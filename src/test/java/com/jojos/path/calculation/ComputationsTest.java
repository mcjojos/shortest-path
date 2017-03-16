package com.jojos.path.calculation;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for {@link Computations} class
 * @author gkaranikas
 */
public class ComputationsTest {

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

    @Test
    public void testVolumetricWeight() {
        double volumetricWeight = Computations.volumetricWeight(26, 10, 11);
        Assert.assertEquals(1d, volumetricWeight, 0d);

        volumetricWeight = Computations.volumetricWeight(26, 10, 9);
        Assert.assertEquals(0.5, volumetricWeight, 0d);

        volumetricWeight = Computations.volumetricWeight(1, 1, 1);
        Assert.assertEquals(0.5, volumetricWeight, 0d);
    }
}
