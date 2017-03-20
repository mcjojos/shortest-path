package com.jojos.path.input;

import com.jojos.path.calculation.DimensionsWeight;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Simply a list of {@link AssertionElement}s
 *
 * @author karanikasg@gmail.com
 */
public class Assertions {

    private final List<AssertionElement> assertionElements;

    public Assertions(List<AssertionElement> assertionElements) {
        this.assertionElements = assertionElements;
    }

    public List<String> getTargets() {
        return assertionElements.stream()
                .map(AssertionElement::getTarget)
                .collect(Collectors.toList());
    }

    public List<DimensionsWeight> getDimensions() {
        return assertionElements.stream()
                .map(AssertionElement::getPackageDimensions)
                .collect(Collectors.toList());
    }

    public List<Double> getShippingCost() {
        return assertionElements.stream()
                .map(AssertionElement::getShippingCost)
                .collect(Collectors.toList());
    }

}
