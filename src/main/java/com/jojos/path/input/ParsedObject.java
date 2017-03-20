package com.jojos.path.input;

import com.jojos.path.calculation.ParseJob;
import org.jgrapht.DirectedGraph;

/**
 * This class contains the outcome of {@link ParseJob#runAndCreateGraph(ParseJob.RunMode)}.
 *
 * i.e. contains both a weighted directed graph together with an object containing the assertions
 * for the particular run.
 *
 * @author karanikasg@gmail.com
 */
public class ParsedObject {

    private final DirectedGraph<String, ParseJob.RelationshipEdge> graph;
    private final Assertions assertions;

    public ParsedObject(DirectedGraph<String, ParseJob.RelationshipEdge> graph, Assertions assertions) {
        this.graph = graph;
        this.assertions = assertions;
    }

    public DirectedGraph<String, ParseJob.RelationshipEdge> getGraph() {
        return graph;
    }

    public Assertions getAssertions() {
        return assertions;
    }
}
