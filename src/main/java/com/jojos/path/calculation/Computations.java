package com.jojos.path.calculation;

/**
 * Provide some static helper and utility methods that assist in computing different aspects of the service
 *
 * @author gkaranikas
 */
public class Computations {

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
     */
    public static double volumetricWeight(double width, double length, double height) {
        double number = (width * length * height) / 5000;
        return roundUpToHalf(number);
    }

    /**
     * This function is simply rounding up a double value by 0.5
     * examples:
     * example:
     * 12.4 --> 12.5
     * 15.5 --> 15.5
     * 22.2 --> 22.5
     * 33.7 --> 34.0
     * 47.9 --> 48.0
     * @param number the number to round up by 0.5
     * @return a number that is rounded up by 0.5
     */
    static double roundUpToHalf(double number) {
        return Math.ceil(number * 2) / 2;
    }
}
