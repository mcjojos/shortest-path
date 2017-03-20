package com.jojos.path.input;

import com.jojos.path.ApplicationException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Contains an ordered collection of {@link InputElement}
 * Always assumes the first one as being the head or source.
 *
 * @author karanikasg@gmail.com
 *
 * @see InputElement
 * @see InputLinesIterator
 */
public class InputLine {
    private final List<InputElement> elements;

    public InputLine(List<InputElement> elements) {
        if (elements == null || elements.isEmpty()) {
            throw new ApplicationException("Input elements are empty");
        }

        long countOfHeads = elements.stream().filter(InputElement::isHead).count();
        if (countOfHeads != 1) {
            throw new ApplicationException("The source at each line must be exactly one");
        }
        if (!elements.get(0).isHead()) {
            throw new ApplicationException("Only the first element is allowed to be the head");
        }
        this.elements = elements;
    }

    public InputElement getSource() {
        return elements.get(0);
    }

    public Set<InputElement> getTargets() {
        return elements.stream().
                filter(inputElement -> !inputElement.isHead()).
                collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return "InputLine{" +
                "elements=" + elements +
                '}';
    }

}
