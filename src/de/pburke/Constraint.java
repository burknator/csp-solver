package de.pburke;

import java.util.ArrayList;
import java.util.Arrays;

public class Constraint {
    public String name = "";
    public ArrayList<SimpleBound> simpleBounds;

    Constraint(String name, SimpleBound[] simpleBounds) {
        this(simpleBounds);
        this.name = name;
    }

    Constraint(SimpleBound[] simpleBounds) {
        this.simpleBounds = new ArrayList<>(Arrays.asList(simpleBounds));
    }

    public boolean isTrue() {
        for (SimpleBound bound : simpleBounds) {
            if (bound.isTrue()) return true;
        }

        return false;
    }

    public boolean isFalse() {
        for (SimpleBound bound : simpleBounds) {
            if (bound.isTrue() || bound.isInconclusive()) return false;
        }

        return true;
    }

    public boolean isInconclusive() {
        for (SimpleBound bound : simpleBounds) {
            if (!bound.isInconclusive()) return false;
        }

        return true;

    public String toString() {
        StringBuilder out = new StringBuilder();
        boolean first = true;
        for (SimpleBound simpleBound : simpleBounds) {
            if (!first) {
                out.append(" ∨ ");
            }
            first = false;
            out.append(simpleBound);
        }

        return out.toString();
    }
}
