package de.pburke;

import java.util.ArrayList;
import java.util.Arrays;

public class Formula {
    public String name = "";
    public ArrayList<Constraint> constraints;
    public Variables variables;

    Formula(String name, Constraint[] constraints) {
        this(constraints);
        this.name = name;
    }

    Formula(Constraint[] constraints) {
        this.constraints = new ArrayList<>(Arrays.asList(constraints));
    }

    public boolean isTrue() {
        for (Constraint constraint : constraints) {
            if (constraint.isFalse() || constraint.isInconclusive()) return false;
        }

        return true;
    }

    public boolean isFalse() {
        for (Constraint constraint : constraints) {
            if (constraint.isFalse()) return true;
        }

        return false;
    }

    public boolean isInconclusive() {
        for (Constraint constraint : constraints) {
            if (!constraint.isInconclusive()) return false;
        }

        return true;

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
