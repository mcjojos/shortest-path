package com.jojos.path.calculation;

/**
 * A class encapsulating the three dimensions of an object or more specifically of a package.
 *
 * @author gkaranikas
 */
public class Dimension {

    private final double width;
    private final double length;
    private final double height;

    public static Dimension of(double width, double length, double height) {
        return new Dimension(width, length, height);
    }

    private Dimension(double width, double length, double height) {
        this.width = width;
        this.length = length;
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public double getLength() {
        return length;
    }

    public double getHeight() {
        return height;
    }
}

