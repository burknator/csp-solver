package de.pburke;

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
        this.x = new Variable(x, x);
        this.y = new Variable(0, 0);
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
}
