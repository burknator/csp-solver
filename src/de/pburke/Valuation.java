package de.pburke;

public class Valuation extends Variable {
    public Variable variable;

    Valuation(Variable variable) throws Exception {
        super(variable.min, variable.max);
        this.variable = variable;
    }

    Valuation(int min, int max, Variable variable) throws Exception {
        super(min, max);
        if (min < variable.min || max > variable.max) {
            throw new Exception("The valuation must conform to the upper and lower bounds of the variable it's for.");
        }

        this.variable = variable;
    }

    public void activate() {
        this.variable.min = this.min;
        this.variable.max = this.max;
    }
}
