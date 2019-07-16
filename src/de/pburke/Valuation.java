package de.pburke;

public class Valuation extends Variable {
    public Variable variable;

    Valuation(int max, int min, Variable variable) throws Exception {
        super(max, min);
        if (max > variable.max || min < variable.min) {
            throw new Exception("The valuation must conform to the upper and lower bounds of the variable it's for.");
        }

        this.variable = variable;
    }
}
