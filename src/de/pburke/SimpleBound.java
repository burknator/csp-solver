package de.pburke;

import de.pburke.exceptions.InvalidVariableCreation;
import jdk.jshell.spi.ExecutionControl;

public class SimpleBound {
    public Variable x;
    public Variable y;
    public int k;

    SimpleBound(Variable x, Variable y, int k) {
        this.x = x;
        this.y = y;
        this.k = k;
    }

    SimpleBound(int x, int k) {
        try {
            this.x = new Variable("CONST_1", x, x);
            this.y = new Variable("CONST_2", 0, 0);
        } catch (InvalidVariableCreation e) { }
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

    public void calculateNewValuation() throws InvalidVariableCreation {
        var xMax = x.max;
        if (!x.isPointInterval()) {
            x.valuation(Math.max(x.min, y.min + k), x.max).activate();
        }

        if (!y.isPointInterval()) {
            y.valuation(y.min, Math.max(y.min, Math.min(y.max, xMax - k))).activate();
        }
    }
}
