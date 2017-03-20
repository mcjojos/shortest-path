package com.jojos.path.calculation;

import com.jojos.path.input.AssertionElement;
import com.jojos.path.input.Assertions;
import com.jojos.path.input.InputElement;
import com.jojos.path.input.InputLine;
import com.jojos.path.input.InputLinesHandler;
import com.jojos.path.input.InputLinesIterator;
import com.jojos.path.input.ParsedObject;
import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.ClassBasedEdgeFactory;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedPseudograph;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.jojos.path.input.AssertionElement.ASSERTION_PREFIX;

/**
 * This class is responsible to parse the file(s) of the friend's network
 * The file has the following format:
 * SOURCE,TARGET:HARD,TARGET:HARD ...
 * This line can appear multiple times.
 *
 * `SOURCE` is You or a friend for whom other friends and their respective hardness relationship is defined.
 * You are always represented by the string `ME`. Your friends are always represented by first name.
 * Friends with same first name are appended a differentiation such as a sequence (Jessica1, Jessica2, Jessica3, etc).
 * For example, you could define your friend network in a `CSV` like this:
 *
 * ME,Lisa:33,Peter:123,John:55
 * Lisa,John:3
 * Diana,Peter:11
 *
 * Given example above, you have three friends: Lisa, Peter and John. You can ship to Lisa @ `33 HARD`,
 * to Peter @ `123 HARD` and to John @ `55 HARD`. In addition, Lisa shipment to John is `3 HARD`
 * and Diana can ship to Peter @ `11 HARD`. You cannot ship to Diana.
 *
 * @author karanikasg@gmail.com
 */
public class ParseJob {

    private final String fileName;
    private final DirectedGraph<String, RelationshipEdge> graph;

    /**
     * The constructor of this class
     * @param fileName the name of the file that the graph shall get loaded from.
     *                 Assertions must also be included in this file.
     */
    public ParseJob(String fileName) {
        this.fileName = fileName;
        this.graph = new DirectedWeightedPseudograph<>(new ClassBasedEdgeFactory<>(RelationshipEdge.class));
    }

    /**
     * After parsing is done we'll have a complete weighted {@link DirectedGraph} constructed
     * together with a potential assertions object
     */
    public ParsedObject runAndCreateGraph(RunMode runMode) {
        List<String[]> lines;

        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            lines = stream.filter(line -> (Objects.nonNull(line) && !line.trim().isEmpty() && !line.startsWith(ASSERTION_PREFIX))).
                    map(line -> line.split("\\s*,\\s*")).
                    filter(strings -> ((Objects.nonNull(strings)) && (strings.length > 1))).
                    collect(Collectors.toList());

            List<InputLine> inputLines = new ArrayList<>();
            for (String[] line : lines) {
                List<InputElement> inputElements = new ArrayList<>();
                for (String element : line) {
                    InputElement inputElement = new InputElement(element);
                    inputElements.add(inputElement);
                }
                inputLines.add(new InputLine(inputElements));
            }
            InputLinesIterator linesIterator = new InputLinesIterator(inputLines);

            linesIterator.forEach(new InputLinesHandler() {
                @Override
                public void handle(InputLine inputLine) {
                    String source = inputLine.getSource().getName();
                    graph.addVertex(source);
                    Set<InputElement> targets = inputLine.getTargets();
                    targets.forEach(target -> {
                        graph.addVertex(target.getName());
                        graph.addEdge(source, target.getName(), new RelationshipEdge<>(source, target.getName(), target.getWeight().getValue()));
                    });
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        Assertions assertions = null;
        if (runMode == RunMode.WITH_ASSERTIONS) {
            try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
                List<AssertionElement> assertionLines = stream.filter(line -> Objects.nonNull(line) && !line.trim().isEmpty()).
                        filter(line -> line.startsWith(ASSERTION_PREFIX)).
                        map(line -> Arrays.asList(line.split("\\s*,\\s*"))).
                        filter(strings -> Objects.nonNull(strings) && strings.size() == 4).
                        map(strings -> strings.subList(1, strings.size())).
                        map(AssertionElement::new).
                        collect(Collectors.toList());
                assertions = new Assertions(assertionLines);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return new ParsedObject(graph, assertions);
    }

    public static class RelationshipEdge<V> extends DefaultWeightedEdge {
        private final V v1;
        private final V v2;
        private final double weight;

        public RelationshipEdge(V v1, V v2, double weight) {
            this.v1 = v1;
            this.v2 = v2;
            this.weight = weight;
        }

        @SuppressWarnings("unused")
        public V getV1() {
            return v1;
        }

        @SuppressWarnings("unused")
        public V getV2() {
            return v2;
        }

        @Override
        public double getWeight() {
            return weight;
        }
    }

    public enum RunMode {
        WITH_ASSERTIONS,
        WITHOUT_ASSERTIONS
    }
}
