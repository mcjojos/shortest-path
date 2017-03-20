package com.jojos.path.calculation;

import com.jojos.path.ApplicationException;
import com.jojos.path.Util;

/**
 * Provide some static helper and utility methods that assist in computing different aspects of the service
 *
 * @author karanikasg@gmail.com
 */
public class Computations {

    /**
     * Parses strings of the following form:
     * WxLxHxW
     * 10x9x5x1200
     * width=10cm, length=9cm, height=5cm, weight=1200grams
     *
     * @return an object of type dimension
     */
    public static DimensionsWeight parseDimensions(String string) {
        String[] array = string.trim().split("x");
        if (array.length != 4) {
            throw new ApplicationException(String.format("Dimensions %s not in the form of WxLxHxW", string));
        }
        Dimensions dimensions = Dimensions.of(
                Util.validateDoubleOrThrow(array[0]),
                Util.validateDoubleOrThrow(array[1]),
                Util.validateDoubleOrThrow(array[2]));
        double weight = Util.validateDoubleOrThrow(array[3]);
        return new DimensionsWeight(dimensions, weight);
    }

    /**
     * The shipping costs are calculated as follows:
     *
     * {shipping cost}(EUR) = sqrt(sum(HARD)) * {normalized weight}(kg)
     *
     * The cost is represented to the nearest two decimals (eg: 24,33 EUR).
     * A normalized package weight is the greater value of an actual weight or a volumetric weight.
     *
     * @param hard the units of costs (or distance if you prefer)
     * @param dimensionsWeight the dimensions and weight combined in one object
     * @return the shipping cost according to the above folrmula
     *
     * @see Computations#normalizedPackageWeight(DimensionsWeight)
     */
    public static double shippingCost(double hard, DimensionsWeight dimensionsWeight) {
        double result = Math.sqrt(hard) * normalizedPackageWeight(dimensionsWeight);
        return Math.round(result * 100d) / 100d;
    }

    /**
     * A normalized package weight is the greater value of an actual weight or a volumetric weight.
     *
     * @param dimensionsWeight the actual weight in kilograms and the dimensions of the package in WxLxH
     * @return the maximum of the actual weight and the so-called volumetric weight of the package
     *
     * @see #volumetricWeight(Dimensions)
     */
    public static double normalizedPackageWeight(DimensionsWeight dimensionsWeight) {
        return Math.max(dimensionsWeight.getWeight(), volumetricWeight(dimensionsWeight.getDimensions()));
    }

    /**
     * A 'volumetric weight' (sometimes called dimensional weight) is a formula often applied by
     * carriers to take into account volume as a function of weight. A light package that
     * takes up a lot of space is just as difficult to ship as a small package that weighs a lot.
     * The volumetric formula is defined as:
     *
     * {Width x Length x Height}(cm) / 5000 = {Volumetric Weight}(kg) rounded up to the nearest 0,5kg
     *
     * A practical example of this is [DHL](http://wap.dhl.com/serv/volweight.html). For example,
     * a package of 'width=26cm, length=10cm and height=11cm' that weighs '400 grams' would
     * have a 'normalized weight = 1kg' because volumetric weight defined as '2860/5000 = 0,572kg'
     * rounded up to the `nearest 0,5kg` is `1kg`.
     *
     * @return a double that will represent the result of the aforementioned calculation
     *
     * @see #roundUpToHalf(double)
     */
    static double volumetricWeight(Dimensions dimensions) {
        double number = (dimensions.getWidth() * dimensions.getLength() * dimensions.getHeight()) / 5_000d;
        return roundUpToHalf(number);
    }

    /**
     * This function is simply rounding up a double value by 0.5
     *
     * examples:
     * 12.4 --> 12.5
     * 15.5 --> 15.5
     * 22.2 --> 22.5
     * 33.7 --> 34.0
     * 47.9 --> 48.0
     *
     * @param number the number to round up by 0.5
     * @return a number that is rounded up by 0.5
     */
    static double roundUpToHalf(double number) {
        return Math.ceil(number * 2) / 2;
    }
}
