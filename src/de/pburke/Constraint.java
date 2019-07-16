package de.pburke;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class Constraint {
    public Collection<SimpleBound> simpleBounds;

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
    }
}
