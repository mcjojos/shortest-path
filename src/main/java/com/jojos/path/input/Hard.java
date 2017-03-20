package com.jojos.path.input;

import com.jojos.path.ApplicationException;

/**
 * Hard is simply a unit of distance measure.
 *
 * Basically a wrapper over a long value.
 *
 * @author karanikasg@gmail.com
 */
public class Hard {

    public static final Hard ZERO = new Hard(0d);

    private final double value;

    public Hard(double value) {
        if (value < 0) {
            throw new ApplicationException("Hard units must have a greater value than zero");
        }
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Hard hard = (Hard) o;

        return Double.compare(hard.value, value) == 0;
    }

    @Override
    public int hashCode() {
        long temp = Double.doubleToLongBits(value);
        return (int) (temp ^ (temp >>> 32));
    }
}
