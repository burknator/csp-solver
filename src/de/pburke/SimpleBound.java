package de.pburke;

import de.pburke.exceptions.InvalidVariableCreation;
import jdk.jshell.spi.ExecutionControl;

/**
 * Represents a simple bound of this form: x ≥ y + k
 * Where x and y are variables and k is a constant integer.
 */
public class SimpleBound {
    public Variable x;
    public Variable y;
    public int k;

    SimpleBound(Variable x, Variable y, int k) {
        this.x = x;
        this.y = y;
        this.k = k;
    }

    /**
     * Create a simple bound using only two integers, making it possible to create a simple bound of this form: 4 ≥ -10
     * 
     * Internally, two point interval variables are created, one with the bounds equal to x, one with the bound equal to
     * 0. k is taken from the parameters of this method. Thus the actual form is x ≥ 0 + k
     * 
     * @param x
     * @param k
     */
    SimpleBound(int x, int k) {
        try {
            this.x = new Variable("CONST_1", x, x);
            this.y = new Variable("CONST_2", 0, 0);
        } catch (InvalidVariableCreation e) {
            // This exception occurs when the lower bound is higher than the upper one. This cannot happen here.
        }
        this.k = k;
    }

    SimpleBound(String toParse) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Not implemented yet.");
    }

    public boolean isTrue() {
        return x.min >= y.max + k;
    }

    public boolean isFalse() {
        return x.max < y.min + k;
    }

    public boolean isInconclusive() {
        return !isTrue() && !isFalse();
    }

    public String toString() {
        return x.name + " ≥ " + y.name + " + " + k;
    }

    /**
     * Deduces new valuations for x and y if they're not point intervals.
     * 
     * FIXME This is more of a {@link CspSolver} thing, maybe put it here? Especially since the containing constraint
     * needs to be unit for this to be doable.
     *
     * @return Whether at least one variable could successfully be narrowed down.
     */
    public boolean deduceValuation() throws InvalidVariableCreation {
        if (isFalse()) return false;

        var xMax = x.max;
        var success = false;
        if (!x.isPointInterval()) {
            var newMin = Math.max(x.min, y.min + k);
            if (newMin > x.min) success = true;
            x.valuation(newMin, x.max).activate();
        }

        if (!y.isPointInterval()) {
            var newMax = Math.max(y.min, Math.min(y.max, xMax - k));
            if (newMax < y.max) success = true;
            y.valuation(y.min, newMax).activate();
        }

        return success;
    }
}
