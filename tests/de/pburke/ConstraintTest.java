package de.pburke;

import de.pburke.exceptions.InvalidVariableCreation;
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
    public void testToString() throws InvalidVariableCreation {
        var x_0 = new Variable("x_0", 0, 3);
        var x_1 = new Variable("x_1", 4, 5);
        var simpleBound = new SimpleBound(x_0, x_1, 5);

        var constraint = new Constraint(new SimpleBound[]{simpleBound, simpleBound});

        assertEquals("x_0 ≥ x_1 + 5 ∨ x_0 ≥ x_1 + 5", constraint.toString());
    }

    @Test
    public void isUnitTrue() throws InvalidVariableCreation {
        var var1 = new Variable("var1", 0, 0);
        var var2 = new Variable("var2", -10, 1);
        var var3 = new Variable("var3", 1, 1);
        var simpleBound = new SimpleBound(var1, var3, 8);
        var simpleBound2 = new SimpleBound(var2, var1, 0);
        assertTrue(simpleBound.isFalse());
        assertTrue(simpleBound2.isInconclusive());

        var constraint = new Constraint(new SimpleBound[]{simpleBound, simpleBound2});
        assertTrue(constraint.isUnit());
    }

    @Test
    public void isUnitFalse() throws InvalidVariableCreation {
        var var1 = new Variable("var1", 0, 100);
        var var2 = new Variable("var2", -10, 1);
        var var3 = new Variable("var3", 1, 1);
        var simpleBound = new SimpleBound(var1, var3, 8);
        var simpleBound2 = new SimpleBound(var2, var1, 0);
        assertTrue(simpleBound.isInconclusive());
        assertTrue(simpleBound2.isInconclusive());

        var constraint = new Constraint(new SimpleBound[]{simpleBound, simpleBound2});
        assertFalse(constraint.isUnit());
    }
}