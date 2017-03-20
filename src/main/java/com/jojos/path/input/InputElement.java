package com.jojos.path.input;

import com.jojos.path.ApplicationException;
import com.jojos.path.Util;

import java.util.Optional;

/**
 * An input element represents a single element that specifies a name of a friend together with an optional weight.
 * If the weight isn't present the object is assumed to be the head of the line.
 *
 * @author karanikasg@gmail.com
 *
 * @see InputLine
 */
public class InputElement {

    private final String name;
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private final Optional<Hard> weight;
    private final boolean isHead;

    /**
     * This constructor will handle both input elements of the forms:
     * Amir:1042
     * and
     * Adam
     * @param element to parse
     */
    public InputElement(String element) {
        String[] elems = element.split(":");
        // this one is the head element consisting of only one name without weight information
        if (elems.length == 1) {
            name = Util.validateNameOrThrow(elems[0]);
            weight = Optional.empty();
            isHead = true;
        } else if (elems.length == 2) {
            name = Util.validateNameOrThrow(elems[0]);
            weight = Optional.of(new Hard(Util.validateLongOrThrow(elems[1])));
            isHead = false;
        } else {
            throw new ApplicationException("Provided element does not follow grammar rules: " + element);
        }
    }

    /**
     * The name of this input element
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * The default value returned in case the weight isn't present is {@link Hard#ZERO}
     * @return the weight
     */
    public Hard getWeight() {
        return weight.orElse(Hard.ZERO);
    }

    /**
     * This the inverse as checking if the weight is present or not
     * @return true if the weight is not present, false otherwise
     */
    public boolean isHead() {
        return isHead;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InputElement that = (InputElement) o;

        if (isHead != that.isHead) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return weight != null ? weight.equals(that.weight) : that.weight == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (weight != null ? weight.hashCode() : 0);
        result = 31 * result + (isHead ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "InputElement{" +
                "name='" + name + '\'' +
                ", weight=" + weight +
                ", isHead=" + isHead +
                '}';
    }
}
