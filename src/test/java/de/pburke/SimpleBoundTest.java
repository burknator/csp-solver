package de.pburke;

import de.pburke.exceptions.InvalidVariableCreation;
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

    @Test
    public void deduceValuation() throws InvalidVariableCreation {
        var x = new Variable("x", 0, 1);
        var y = new Variable("y", 4, 20);
        var z = new Variable("z", 10,15);
        var bound1 = new SimpleBound(x, y, 0);
        var bound2 = new SimpleBound(y, z, 4);
        assertTrue(bound1.isFalse());
        assertTrue(bound2.isInconclusive());

        var success = bound1.deduceValuation();
        assertFalse(success);

        success = bound2.deduceValuation();
        assertTrue(success);

        assertEquals(14, y.min);
        assertEquals(20, y.max);
        assertEquals(10, z.min); // Unchanged
        assertEquals(15, z.max); // Unchanged
    }
}