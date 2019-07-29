package de.pburke;

import de.pburke.exceptions.InvalidVariables;

import java.util.ArrayList;
import java.util.Arrays;

public class Variables {
    private Boolean valid = null;
    private int variableIndex = 0;
    private ArrayList<Variable> variables = new ArrayList<>();

    public Variables() { }

    public Variables(ArrayList<Variable> list) {
        this.variables.addAll(list);
        checkValidity();
    }

    public Variables(Variable... variables) {
        this.variables.addAll(Arrays.asList(variables));
        checkValidity();
    }

    private void checkValidity() {
        for (Variable variable : variables) {
            if (!variable.isPointInterval()){
                valid = true;
                return;
            }
        }

        valid = false;
    }

    public boolean isValid() {
        if (valid == null) checkValidity();

        return valid;
    }

    public Variable getSplitVariable() throws InvalidVariables {
        if (!isValid())
            throw new InvalidVariables("The set of variables of this formula are invalid.");

        Variable splitVariable;

        do {
            splitVariable = variables.get(variableIndex);
            variableIndex = (variableIndex + 1) % variables.size();
        } while(splitVariable.isPointInterval());

        return splitVariable;
    }

    public void logValuation() {
        Logger.log("Valuation:");
        for (Variable variable : variables) {
            Logger.log(variable.name + ": " + variable, 1);
        }
    }
}
