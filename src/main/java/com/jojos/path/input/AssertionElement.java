package com.jojos.path.input;

import com.jojos.path.ApplicationException;
import com.jojos.path.Util;
import com.jojos.path.calculation.Computations;
import com.jojos.path.calculation.DimensionsWeight;

import java.util.List;
import java.util.Objects;

/**
 * Represents a line of assertion.
 * This line has the following format:
 *
 * @,Lisa,10x9x5x1200,6.89
 * @,Diana,6x10x8x1233,~
 *
 * The first assertion states that a shipping cost for a `1.2kg (width=10cm, length=9cm, height=5cm)`
 * package to Lisa should be `6.89 EUR`. The second assertion states that it's impossible to ship a
 * package to Diana so the cost is a positive infinity.

 * @author karanikasg@gmail.com
 */
public class AssertionElement {
    public static final String ASSERTION_PREFIX = "@";

    private final String target;
    private final DimensionsWeight packageDimensions;
    private final double shippingCost;

    public AssertionElement(List<String> list) {
        if (Objects.isNull(list) || list.size() != 3) {
            throw new ApplicationException("Wrong format of assertion statement: " + list);
        }
        this.target = Util.validateNameOrThrow(list.get(0));
        this.packageDimensions = Computations.parseDimensions(list.get(1));
        this.shippingCost = Util.validateDoubleOrThrow(list.get(2));
    }

    public String getTarget() {
        return target;
    }

    public DimensionsWeight getPackageDimensions() {
        return packageDimensions;
    }

    public double getShippingCost() {
        return shippingCost;
    }
}
