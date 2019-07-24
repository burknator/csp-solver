package de.pburke;

public class Variable {
    public int max;
    public int min;

    public Variable(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public boolean isPointInterval() {
        return min == max;
    }

    public Valuation valuation(int min, int max) throws Exception {
        return new Valuation(min, max, this);
    }

    public Valuation asValuation() {
        return new Valuation(this);
    }
}
