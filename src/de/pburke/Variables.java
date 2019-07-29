package de.pburke;

import de.pburke.exceptions.InvalidVariables;

import java.util.ArrayList;
import java.util.Arrays;

public class Variables {
    /**
     * Stores whether this set of variables is valid. Null means no check has been done yet.
     */
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

    /**
     * Check if this set of variables is valid.
     * 
     * A set of variables is valid, iff at least one non-point-interval variable is part of this set.
     * 
     * FIXME This is actually something which the {CspSolver} should decide. Maybe put this class into it?
     */
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

    /**
     * Searches for the next non-point-interval variable and returns it.
     *
     * With each call the index into the internal variable storage is increased by one and reset to zero when the end of
     * the storage is reached. This way, the variables are returned in a round-robin manner.
     *
     * @return A variable which is not a point interval.
     * @throws InvalidVariables
     */
    public Variable findSplitVariable() throws InvalidVariables {
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
