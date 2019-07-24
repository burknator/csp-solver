package de.pburke;

public class Formula {
    public Constraint[] constraints;
    public Variables variables;

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
