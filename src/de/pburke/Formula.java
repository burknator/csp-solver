package de.pburke;

import java.util.ArrayList;
import java.util.Arrays;

public class Formula {
    public ArrayList<Constraint> constraints;
    public Variables variables;

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
    }
}
