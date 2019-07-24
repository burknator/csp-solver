package de.pburke;

import de.pburke.exceptions.InvalidVariables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class Variables {
    private Boolean valid = null;
    private int variableIndex = 0;
    private ArrayList<Variable> variables = new ArrayList<>();

    public Variables() { }

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

    public Variable get(int index) {
        return variables.get(index);
    }

    public boolean addAll(Collection<Variable> collection) {
        return this.variables.addAll(collection);
    }

    public boolean isValid() {
        if (valid == null) checkValidity();

        return valid;
    }

    public Variable getSplitVariable() throws Exception {
        if (!isValid())
            throw new InvalidVariables("The set of variables of this formula are invalid.");

        Variable splitVariable;

        do {
            splitVariable = variables.get(variableIndex);
            variableIndex = (variableIndex + 1) % variables.size();
        } while(splitVariable.isPointInterval());

        return splitVariable;
    }
}
