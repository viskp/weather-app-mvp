package com.vishal.weatherapp.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test class for {@link Utils}.
 *
 * @author Vishal - 25th Feb 2019
 * @since 1.0.0
 */
public class UtilsTest {
    @Test
    public void addDegreeSymbol() {
        assertEquals("-°", Utils.addDegreeSymbol(null));
        assertEquals("0°", Utils.addDegreeSymbol(0.0));
        assertEquals("28°", Utils.addDegreeSymbol(27.9));
        assertEquals("27°", Utils.addDegreeSymbol(27.1));
    }
}