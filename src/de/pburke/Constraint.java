package de.pburke;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Represents a constraint: simpleBound1 ∨ simpleBound2 ∨ ...
 */
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

    /**
     * @return True, when at least one bound true.
     */
    public boolean isTrue() {
        for (SimpleBound bound : simpleBounds) {
            if (bound.isTrue()) return true;
        }

        return false;
    }

    /**
     * @return True, when all bounds are false.
     */
    public boolean isFalse() {
        for (SimpleBound bound : simpleBounds) {
            if (bound.isTrue() || bound.isInconclusive()) return false;
        }

        return true;
    }

    /**
     * @return True, when at least one bound is inconclusive.
     */
    public boolean isInconclusive() {
        for (SimpleBound bound : simpleBounds) {
            if (bound.isInconclusive()) return true;
        }

        return false;
    }

    /**
     * @return True if all but one bound is false, and that one is inconclusive.
     */
    public boolean isUnit() {
        int i = 0;
        int f = 0;
        for (SimpleBound bound : simpleBounds) {
            if (bound.isInconclusive()) i++;
            else if (bound.isFalse()) f++;
        }

         return i == 1 && i + f == simpleBounds.size();
    }

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
