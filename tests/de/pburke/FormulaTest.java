package de.pburke;

import org.junit.Test;

import java.text.Normalizer;

import static org.junit.Assert.*;

public class FormulaTest {

    @Test
    public void isTrue() throws Exception {
        var x_0 = new Variable("x_0", 1, 3);
        var x_1 = new Variable("x_1", 0,0);
        var simpleBound = new SimpleBound(x_0, x_1, 1);
        var constraint = new Constraint(new SimpleBound[]{simpleBound});
        var formula = new Formula(new Constraint[]{constraint});

        assertTrue(formula.isTrue());
    }

    @Test
    public void isFalse() throws Exception {
        var x_0 = new Variable("x_0", 0, 0);
        var x_1 = new Variable("x_1", 1,1);
        var x_2 = new Variable("x_2", -2, 3);

        var simpleBound = new SimpleBound(x_0, x_2, 2);
        assertFalse(simpleBound.isTrue());
        assertFalse(simpleBound.isFalse());
        assertTrue(simpleBound.isInconclusive());

        var simpleBound1 = new SimpleBound(x_1, x_2, 4);
        assertFalse(simpleBound1.isTrue());
        assertTrue(simpleBound1.isFalse());
        assertFalse(simpleBound1.isInconclusive());

        var constraint = new Constraint(new SimpleBound[]{simpleBound});
        assertFalse(constraint.isTrue());
        assertFalse(constraint.isFalse());
        assertTrue(constraint.isInconclusive());

        var constraint1 = new Constraint(new SimpleBound[]{simpleBound1});
        assertFalse(constraint1.isTrue());
        assertTrue(constraint1.isFalse());
        assertFalse(constraint1.isInconclusive());

        var formula = new Formula(new Constraint[]{constraint, constraint1});
        assertFalse(formula.isTrue());
        assertFalse(formula.isFalse());
        assertTrue(formula.isInconclusive());
    }

    @Test
    public void isInconclusive() {
    }

    @Test
    public void testToString() throws Exception {
        var x_0 = new Variable("x_0", 0, 3);
        var x_1 = new Variable("x_1", 4, 5);
        var simpleBound = new SimpleBound(x_0, x_1, 5);

        var constraint = new Constraint(new SimpleBound[]{simpleBound});
        var constraint2 = new Constraint(new SimpleBound[]{simpleBound});

        var formula = new Formula(new Constraint[]{constraint, constraint2});

        assertEquals("x_0 ≥ x_1 + 5 ∧ x_0 ≥ x_1 + 5", formula.toString());
    }
}