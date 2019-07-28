package de.pburke;

import de.pburke.exceptions.InvalidVariableCreation;

public class Variable {
    public String name = "";
    public int max;
    public int min;

    public Variable(String name, int min, int max) throws InvalidVariableCreation {
        this(min, max);
        this.name = name;
    }

    public Variable(int min, int max) throws InvalidVariableCreation {
        if (min > max)
            throw new InvalidVariableCreation("Invalid variable, min must be smaller or equal than max.");

        this.min = min;
        this.max = max;
    }

    public boolean isPointInterval() {
        return min == max;
    }

    public Valuation valuation(int min, int max) throws InvalidVariableCreation {
        return new Valuation(min, max, this);
    }

    public String toString() {
        return "[" + this.min + ", " + this.max + "]";
    }
}
