package com.jojos.path.calculation;

import com.jojos.path.ApplicationException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for {@link Computations} class
 *
 * @author karanikasg@gmail.com
 */
public class ComputationsTest {

    private final Dimensions dimensions1 = Dimensions.of(26, 10, 11);
    private final Dimensions dimensions2 = Dimensions.of(26, 10, 9);
    private final Dimensions dimensions3 = Dimensions.of(1, 1, 1);

    @Test
    public void testShippingCosts() {
        double hard1 = 33;
        DimensionsWeight dimensionsWeight1 = new DimensionsWeight(Dimensions.of(10, 9, 5), 1_200);
        double result1 = Computations.shippingCost(hard1, dimensionsWeight1);
        Assert.assertEquals(6.89, result1, 0);

        double hard2 = 128;
        DimensionsWeight dimensionsWeight2 = new DimensionsWeight(dimensions1, 500);
        double result2 = Computations.shippingCost(hard2, dimensionsWeight2);
        Assert.assertEquals(11.31, result2, 0);

        double hard3 = 17;
        DimensionsWeight dimensionsWeight3 = new DimensionsWeight(dimensions1, 500);
        double result3 = Computations.shippingCost(hard3, dimensionsWeight3);
        Assert.assertEquals(4.12, result3, 0);

    }

    @Test(expected = ApplicationException.class)
    public void testParseIncorrectDimensions() {
        String str = "2x3x4";
        Computations.parseDimensions(str);
    }

    @Test(expected = ApplicationException.class)
    public void testParseEmptyDimensions() {
        String str = "";
        Computations.parseDimensions(str);
    }

    @Test
    public void testParseDimension() {
        String str = "2x3x4x1200";
        DimensionsWeight dimensionsWeight = Computations.parseDimensions(str);
        Assert.assertEquals(dimensionsWeight.getWidth(), 2, 0);
        Assert.assertEquals(dimensionsWeight.getLength(), 3, 0);
        Assert.assertEquals(dimensionsWeight.getHeight(), 4, 0);
        // grams get converted in kilograms
        Assert.assertEquals(dimensionsWeight.getWeight(), 1.2, 0);
    }

    @Test
    public void testNormalizedPackageWeight() {
        double normalizedPackageWeight1 = Computations.normalizedPackageWeight(new DimensionsWeight(dimensions1, 2_000));
        Assert.assertEquals(2, normalizedPackageWeight1, 0);

        double normalizedPackageWeight2 = Computations.normalizedPackageWeight(new DimensionsWeight(dimensions1, 500));
        Assert.assertEquals(1, normalizedPackageWeight2, 0);

        double normalizedPackageWeight3 = Computations.normalizedPackageWeight(new DimensionsWeight(dimensions2, 5_000));
        Assert.assertEquals(5, normalizedPackageWeight3, 0);

        double normalizedPackageWeight4 = Computations.normalizedPackageWeight(new DimensionsWeight(dimensions2, 100));
        Assert.assertEquals(0.5, normalizedPackageWeight4, 0);

        double normalizedPackageWeight5 = Computations.normalizedPackageWeight(new DimensionsWeight(dimensions3, 9_100));
        Assert.assertEquals(9.1, normalizedPackageWeight5, 0);

        double normalizedPackageWeight6 = Computations.normalizedPackageWeight(new DimensionsWeight(dimensions3, 0));
        Assert.assertEquals(0.5, normalizedPackageWeight6, 0);

    }

    @Test
    public void testVolumetricWeight() {
        double volumetricWeight1 = Computations.volumetricWeight(dimensions1);
        Assert.assertEquals(1d, volumetricWeight1, 0d);

        double volumetricWeight2 = Computations.volumetricWeight(dimensions2);
        Assert.assertEquals(0.5, volumetricWeight2, 0d);

        double volumetricWeight3 = Computations.volumetricWeight(dimensions3);
        Assert.assertEquals(0.5, volumetricWeight3, 0d);
    }

    @Test
    public void testRoundUp() {
        double result = Computations.roundUpToHalf(12.4);
        Assert.assertEquals(12.5, result, 0);

        result = Computations.roundUpToHalf(15.5);
        Assert.assertEquals(15.5, result, 0);

        result = Computations.roundUpToHalf(22.2);
        Assert.assertEquals(22.5, result, 0);

        result = Computations.roundUpToHalf(33.7);
        Assert.assertEquals(34, result, 0);

        result = Computations.roundUpToHalf(47.9);
        Assert.assertEquals(48, result, 0);
    }

}
