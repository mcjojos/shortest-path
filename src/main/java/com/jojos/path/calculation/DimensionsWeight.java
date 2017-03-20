package com.jojos.path.calculation;

/**
 * Class that represents exactly what the name suggests: a dimensions object and its corresponding weight in kilograms
 *
 * @author karanikasg@gmail.com
 */
public class DimensionsWeight {
    private final Dimensions dimensions;
    private final double weight;

    public DimensionsWeight(Dimensions dimensions, double weight) {
        this.dimensions = dimensions;
        this.weight = weight / 1000d;
    }

    public Dimensions getDimensions() {
        return dimensions;
    }

    public double getWidth() {
        return getDimensions().getWidth();
    }

    public double getLength() {
        return getDimensions().getLength();
    }

    public double getHeight() {
        return getDimensions().getHeight();
    }


    public double getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "DimensionsWeight{" +
                "dimensions=" + dimensions +
                ", weight=" + weight +
                '}';
    }
}
