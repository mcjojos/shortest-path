package com.jojos.path.calculation;

/**
 * A class encapsulating the three dimensions of an object or more specifically of a package
 * in the form WxLxH
 *
 * @author karanikasg@gmail.com
 */
public class Dimensions {

    private final double width;
    private final double length;
    private final double height;

    public static Dimensions of(double width, double length, double height) {
        return new Dimensions(width, length, height);
    }

    private Dimensions(double width, double length, double height) {
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

    @Override
    public String toString() {
        return "Dimensions{" +
                "width=" + width +
                ", length=" + length +
                ", height=" + height +
                '}';
    }
}

