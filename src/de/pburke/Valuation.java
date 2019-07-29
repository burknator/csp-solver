package de.pburke;

import de.pburke.exceptions.InvalidVariableCreation;

public class Valuation extends Variable {
    public Variable variable;

    Valuation(Variable variable) throws InvalidVariableCreation {
        super(variable.min, variable.max);
        this.variable = variable;
    }

    Valuation(int min, int max, Variable variable) throws InvalidVariableCreation {
        super(min, max);
        if (min < variable.min || max > variable.max) {
            throw new InvalidVariableCreation("The valuation must conform to the upper and lower bounds of the variable it's for.");
        }

        this.variable = variable;
    }

    public void activate() {
        if (!variable.name.equals("")) {
            Logger.log("Changing valuation for " + variable.name + " from " + variable + " to " + this);
        }

        this.variable.min = this.min;
        this.variable.max = this.max;
    }
}
