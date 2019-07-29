package de.pburke;

import de.pburke.exceptions.InvalidVariableCreation;

public class Variable {
    /**
     * This makes it easier for a user to identify this variable in the log.
     */
    public String name = "";
    public int max;
    public int min;


    /**
     * {@inheritDoc}
     * @param name The name for this variable.
     */
    public Variable(String name, int min, int max) throws InvalidVariableCreation {
        this(min, max);
        this.name = name;
    }

    /**
     * @param min The lower bound of this variable.
     * @param max The upper bound of this variable.
     * @throws InvalidVariableCreation When min is lower than max.
     */
    public Variable(int min, int max) throws InvalidVariableCreation {
        if (min > max)
            throw new InvalidVariableCreation("Invalid variable, min must be smaller or equal than max.");

        this.min = min;
        this.max = max;
    }

    /**
     * A variable is a point interval, when the lower and upper bounds are equal.
     */
    public boolean isPointInterval() {
        return min == max;
    }

    /**
     * Creates a new valuation, which encapsulates a reference to this variable.
     * @param min The lower bound of the valuation.
     * @param max The upper bound of the valuation.
     * @return An instance of Valuation of this variable.
     * @throws InvalidVariableCreation
     */
    public Valuation valuation(int min, int max) throws InvalidVariableCreation {
        return new Valuation(min, max, this);
    }

    public String toString() {
        return "[" + this.min + ", " + this.max + "]";
    }
}
