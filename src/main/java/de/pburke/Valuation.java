package de.pburke;

import de.pburke.exceptions.InvalidVariableCreation;

public class Valuation extends Variable {
    /**
     * The variable this valuation is for.
     *
     * This allows activating a valuation even if a previous one was already activated for this variable simply by
     * storing the Valuation instance.
     */
    public Variable variable;

    /**
     * Create a valuation using the bounds from the given variable as-is.
     * 
     * Creating an instance of valuation does not actually change the valuation of a variable, to do this, see
     * {@link #activate()}.
     * 
     * @see Variable#Variable(int, int)
     */
    Valuation(Variable variable) throws InvalidVariableCreation {
        super(variable.min, variable.max);
        this.variable = variable;
    }

    /**
     * Creating an instance of valuation does not actually change the valuation of a variable, to do this, see
     * {@link #activate()}.
     * 
     * @param min The lower bound of this valuation.
     * @param max The upper bound of this valuation.
     * @param variable The variable that this instance is a valuation of.
     * @throws InvalidVariableCreation Then the lower and upper bounds are not within the ones from the variable.
     */
    Valuation(int min, int max, Variable variable) throws InvalidVariableCreation {
        super(min, max);
        if (min < variable.min || max > variable.max) {
            throw new InvalidVariableCreation("The valuation must conform to the upper and lower bounds of the " +
                                              "variable it's for.");
        }

        this.variable = variable;
    }

    /**
     * Actually change the valuation of the variable this instance is a valuation of.
     * 
     * This overwrites the bounds of the referenced variable, thus changing it's valuation. The old values are lost.
     */
    public void activate() {
        if (!variable.name.equals("")) {
            Logger.log("Changing valuation for " + variable.name + " from " + variable + " to " + this);
        }

        this.variable.min = this.min;
        this.variable.max = this.max;
    }
}
