package de.pburke;

public class Variable {
    public int max;
    public int min;

    public Variable(int max, int min) {
        this.max = max;
        this.min = min;
    }

    public Valuation valuation(int max, int min) throws Exception {
        return new Valuation(max, min, this);
    }
}
