package de.pburke;

import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleBoundTest {

    @Test
    public void isTrue() {
    }

    @Test
    public void isFalse() {
    }

    @Test
    public void isInconclusive() {
        var bound = new SimpleBound(4, -10);
        assertTrue(bound.isTrue());
        assertFalse(bound.isFalse());
        assertFalse(bound.isInconclusive());
    }
}