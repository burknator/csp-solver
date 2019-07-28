package de.pburke;

import org.junit.Test;

import static org.junit.Assert.*;

public class ConstraintTest {

    @Test
    public void isTrue() {
    }

    @Test
    public void isFalse() {
    }

    @Test
    public void isInconclusive() {
    }

    @Test
    public void testToString() throws Exception {
        var x_0 = new Variable("x_0", 0, 3);
        var x_1 = new Variable("x_1", 4, 5);
        var simpleBound = new SimpleBound(x_0, x_1, 5);

        var constraint = new Constraint(new SimpleBound[]{simpleBound, simpleBound});

        assertEquals("x_0 ≥ x_1 + 5 ∨ x_0 ≥ x_1 + 5", constraint.toString());
    }
}