package de.pburke;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Represents a formula: constraint1 ∧ constraint2 ∧ ...
 */
public class Formula {
    public String name = "";
    public ArrayList<Constraint> constraints;

    Formula(String name, Constraint[] constraints) {
        this(constraints);
        this.name = name;
    }

    Formula(Constraint[] constraints) {
        this.constraints = new ArrayList<>(Arrays.asList(constraints));
    }

    /**
     * @return True, when all constraints are true.
     */
    public boolean isTrue() {
        for (Constraint constraint : constraints) {
            if (constraint.isFalse() || constraint.isInconclusive()) return false;
        }

        return true;
    }

    public boolean isFalse() {
        for (Constraint constraint : constraints) {
            if (constraint.isTrue() || constraint.isInconclusive()) return false;
        }

        return true;
    }

    /**
     * @return True, when at least one constraint is inconclusive.
     */
    public boolean isInconclusive() {
        for (Constraint constraint : constraints) {
            if (constraint.isInconclusive()) return true;
        }

        return false;
    }

    public String toString() {
        StringBuilder out = new StringBuilder();
        var first = true;
        for (Constraint constraint : constraints) {
            if (!first) {
                out.append(" ∧ ");
            }
            first = false;
            out.append(constraint.toString());
        }

        return out.toString();
    }
}
