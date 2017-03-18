package com.jojos.path.calculation;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Unit tests for {@link Computations} class
 * @author gkaranikas
 */
public class ComputationsTest {

    private final Dimension dimension1 = Dimension.of(26, 10, 11);
    private final Dimension dimension2 = Dimension.of(26, 10, 9);
    private final Dimension dimension3 = Dimension.of(1, 1, 1);


    @Test
    public void testNormalizedPackageWeight() {
        double normalizedPackageWeight1 = Computations.normalizedPackageWeight(2, dimension1);
        Assert.assertEquals(2, normalizedPackageWeight1, 0);

        double normalizedPackageWeight2 = Computations.normalizedPackageWeight(0.5, dimension1);
        Assert.assertEquals(1, normalizedPackageWeight2, 0);

        double normalizedPackageWeight3 = Computations.normalizedPackageWeight(5, dimension2);
        Assert.assertEquals(5, normalizedPackageWeight3, 0);

        double normalizedPackageWeight4 = Computations.normalizedPackageWeight(0.1, dimension2);
        Assert.assertEquals(0.5, normalizedPackageWeight4, 0);

        double normalizedPackageWeight5 = Computations.normalizedPackageWeight(9.1, dimension3);
        Assert.assertEquals(9.1, normalizedPackageWeight5, 0);

        double normalizedPackageWeight6 = Computations.normalizedPackageWeight(0, dimension3);
        Assert.assertEquals(0.5, normalizedPackageWeight6, 0);

    }

    @Test
    public void testVolumetricWeight() {
        double volumetricWeight1 = Computations.volumetricWeight(dimension1);
        Assert.assertEquals(1d, volumetricWeight1, 0d);

        double volumetricWeight2 = Computations.volumetricWeight(dimension2);
        Assert.assertEquals(0.5, volumetricWeight2, 0d);

        double volumetricWeight3 = Computations.volumetricWeight(dimension3);
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
