package com.jojos.path.calculation;

import com.jojos.path.ApplicationException;
import com.jojos.path.Util;
import com.jojos.path.input.Assertions;
import com.jojos.path.input.ParsedObject;
import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A job whose sole purpose it to compute the shortest path of a weighted directed graph
 *
 * @author karanikasg@gmail.com
 */
public class ShortestPathJob {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private static final String ME = "ME";

    private final DirectedGraph<String, ParseJob.RelationshipEdge> graph;
    private final Assertions assertions;
    private final String outputFileName;
    private final Collection<String> sources;
    private final Collection<String> targets;
    private final DimensionsWeight dimensionsWeight;

    private ShortestPathJob(DirectedGraph<String, ParseJob.RelationshipEdge> graph,
                           Assertions assertions,
                           String outputFileName,
                           Collection<String> sources,
                           Collection<String> targets,
                           DimensionsWeight dimensionsWeight) {
        this.graph = graph;
        this.assertions = assertions;
        this.outputFileName = outputFileName;
        this.sources = sources;
        this.targets = targets;
        this.dimensionsWeight = dimensionsWeight;
    }

    /**
     * Running with assertions ON will make all the other parameters
     * default to null and don't take them into account
     *
     * @param parsedObject the graph created on a previous step together with assertions to assert the results against
     */
    public static ShortestPathJob withAssertions(ParsedObject parsedObject) {
        return new ShortestPathJob(parsedObject.getGraph(),
                parsedObject.getAssertions(),
                null,
                null,
                null,
                null);
    }

    /**
     * This is the running mode without assertions
     * @param graph the graph created on a previous step
     * @param outputFileName the file name of the output
     * @param source the starting vertex of our graph. If null it will default to all of the vertices contained in this graph
     * @param target the ending vertex. It null it will default to all of the vertices contained in this graph
     * @param dimensionsWeight the dimensions and weight object for which we must calculate the shipping costs
     */
    public static ShortestPathJob withoutAssertions(DirectedGraph<String, ParseJob.RelationshipEdge> graph,
                                             String outputFileName,
                                             String source,
                                             String target,
                                             DimensionsWeight dimensionsWeight) {
        Collection<String> sources = source == null ? graph.vertexSet() : Collections.singleton(source);
        Collection<String> targets = target == null ? graph.vertexSet() : Collections.singleton(target);
        return new ShortestPathJob(graph, null, outputFileName, sources, targets, dimensionsWeight);
    }

    /**
     * The run function will calculate the shortest path between vertices and calculate the shipping costs
     * if dimensions are passed
     */
    public void run() {
        // immediately return if no vertices or edges are found
        if (graphIsInvalid()) {
            return;
        }

        log.info("Start calculating the graph for sources {}", sources);

        DijkstraShortestPath<String, ParseJob.RelationshipEdge> dijkstraAlg =
                new DijkstraShortestPath<>(graph);

        // running with assertions on
        if (assertions != null) {
            String source = ME;
            ShortestPathAlgorithm.SingleSourcePaths<String, ParseJob.RelationshipEdge> iPaths = dijkstraAlg.getPaths(source);
            for (int i = 0; i < assertions.getTargets().size(); i++) {
                String target = assertions.getTargets().get(i);
                DimensionsWeight packageDimensions = assertions.getDimensions().get(i);
                double assertionCost = assertions.getShippingCost().get(i);
                if (iPaths.getPath(target) == null) {
                    // if there isn't any path ensure that it's also reflected in the assertions (only if it contains the equivalent line)
                    if (assertionCost == Double.POSITIVE_INFINITY) {
                        log.info(String.format("[%s -> %s] - [Cost: %f]", source, target, assertionCost));
                        log.info("Shipping cost defined for path from {} -> {} is correctly asserted to {} (doesn't exist)", source, target, Double.POSITIVE_INFINITY);
                    } else {
                        throw new AssertionError(String.format("Path from %s -> %s doesn't seem to exist but assertion cost has a value of %.2f. Exiting", source, target, assertionCost));
                    }
                } else {
                    if (packageDimensions != null) {
                        double pathWeight = dijkstraAlg.getPathWeight(source, target);
                        double shippingCost = Computations.shippingCost(pathWeight, packageDimensions);

                        String formattedStr = String.format("%s - [Cost: %s]",
                                Util.toPrettyPathString(iPaths.getPath(target).getVertexList()),
                                shippingCost);
                        log.info(formattedStr);
                        if (Double.compare(shippingCost, assertionCost) != 0) {
                            throw new AssertionError(String.format("%.2f is not equal to shipping cost defined in assertion %.2f. Exiting", shippingCost, assertionCost));
                        } else {
                            log.info("Shipping cost defined for path from {} -> {} is correctly asserted to {}", source, target, shippingCost);
                        }
                    }
                }
            }
        } else { // running without assertions
            List<String> lines = new ArrayList<>();

            for (String source : sources) {
                ShortestPathAlgorithm.SingleSourcePaths<String, ParseJob.RelationshipEdge> iPaths = dijkstraAlg.getPaths(source);
                for (String target : targets) {
                    // exclude self reference nodes even if they have an infinite distance
                    if (target.equals(source)) {
                        continue;
                    }

                    if (iPaths.getPath(target) != null) {
                        double pathWeight = dijkstraAlg.getPathWeight(source, target);
                        String weightStr = "Weight";
                        if (dimensionsWeight != null) {
                            pathWeight = Computations.shippingCost(pathWeight, dimensionsWeight);
                            weightStr = "Cost";
                        }

                        String formattedStr = String.format("%s - [%s: %s]",
                                Util.toPrettyPathString(iPaths.getPath(target).getVertexList()),
                                weightStr,
                                pathWeight);
                        log.info(formattedStr);
                        lines.add(formattedStr);
                    }
                }
            }
            try {
                Files.write(Paths.get(outputFileName), lines);
            } catch (IOException e) {
                throw new ApplicationException("Cannot write to " + outputFileName);
            }
        }
    }

    private boolean graphIsInvalid() {
        final boolean invalid = true;
        if (Objects.isNull(graph) || Objects.isNull(graph.vertexSet()) || graph.vertexSet().isEmpty()) {
            log.warn("Graph contains no vertices...exiting");
            return invalid;
        } else if (Objects.isNull(graph.edgeSet()) || graph.edgeSet().isEmpty()) {
            log.warn("Graph with no edges between vertices");
            return invalid;
        }
        return !invalid;
    }

}
