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

    /**
     * @return True, when no constraints are inconclusive and at least one constrait is false. False, when all
     * constraints are true.
     */
    public boolean isFalse() {
        boolean f = false;
        for (Constraint constraint : constraints) {
            // We need to go through all constraint to make sure none are inconclusive. But once we found one, we can
            // exit early.
            if (constraint.isInconclusive()) return false;
            if (constraint.isFalse()) f = true;
        }

        return f;
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
