package de.pburke;

public class Variable {
    public String name = "";
    public int max;
    public int min;

    public Variable(String name, int min, int max) throws Exception {
        this(min, max);
        this.name = name;
    }

        this.min = min;
        this.max = max;
    }

    public boolean isPointInterval() {
        return min == max;
    }

    public Valuation valuation(int min, int max) throws Exception {
        return new Valuation(min, max, this);
    }

    public String toString() {
        return "[" + this.min + ", " + this.max + "]";
    }
}
