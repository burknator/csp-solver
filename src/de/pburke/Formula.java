package de.pburke;

import java.util.ArrayList;
import java.util.Iterator;

public class Formula {
    public Constraint[] constraints;
    public ArrayList<Variable> variables;

    Formula(Constraint[] constraints) {
        this.constraints = constraints;
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
