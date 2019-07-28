package de.pburke;

import de.pburke.exceptions.BaseException;
import de.pburke.exceptions.InvalidVariableCreation;
import de.pburke.exceptions.InvalidVariables;

import java.util.Stack;

public class CspSolver {
    public Formula formula;
    private Stack<Valuation> backtrackAlternatives = new Stack<>();

    enum State {
        CONSISTENCY_CHECK, BACKTRACK, DECISION, SATISFIABLE, NOT_SATISFIABLE
    }

    enum Result {
        SATISFIABLE, NOT_SATISFIABLE
    }

    public CspSolver() { }

    public CspSolver(Formula formula) {
        this.formula = formula;
    }

    public int getDecisionLevel() {
        return backtrackAlternatives.size();
    }

    public Result start() throws BaseException {
        var currentState = State.CONSISTENCY_CHECK;

        mainLoop: while (true) {
            log("-".repeat(72));
            switch (currentState) {
                case CONSISTENCY_CHECK:
                    currentState = consistencyCheck();
                    break;
                case BACKTRACK:
                    currentState = backtrack();
                    break;
                case DECISION:
                    currentState = decision();
                    break;
                case SATISFIABLE:
                    return Result.SATISFIABLE;
                case NOT_SATISFIABLE:
                    return Result.NOT_SATISFIABLE;
                default:
                    throw new BaseException("Something horrible must've happened!");
            }
        }
    }

    public State consistencyCheck() throws InvalidVariableCreation {
        log("Making consistency check");
        if (formula.isTrue()) {
            log("Formula " + formula.name + " is true:");
            for (Constraint constraint : formula.constraints) {
                log("Constraint " + constraint.name + " is true:", 1);
                for (SimpleBound bound : constraint.simpleBounds) {
                    if (bound.isTrue()) {
                        log("Simple bound " + bound + " is true with valuation:", 2);
                        log(bound.x.name + ": " + bound.x, 3);
                        log(bound.y.name + ": " + bound.y, 3);
                        log("k: " + bound.k, 3);
                    }
                }
            }

            return State.SATISFIABLE;
        }

        if (formula.isFalse()) {
            log("Formula " + formula.name + " is false:");
            for (Constraint constraint : formula.constraints) {
                if (constraint.isFalse()) {
                    log("Constraint " + constraint.name + " is false:", 1);
                    for (SimpleBound bound : constraint.simpleBounds) {
                        if (bound.isFalse()) {
                            log("Simple bound " + bound + " is false with valuation:", 2);
                            log(bound.x.name + ": " + bound.x, 3);
                            log(bound.y.name + ": " + bound.y, 3);
                            log("k: " + bound.k, 3);
                        }
                    }
                }
            }
            return State.BACKTRACK;
        }

        log("Formula " + formula.name + " is inconclusive:");
        for (Constraint constraint : formula.constraints) {
            if (constraint.isInconclusive()) {
                log("Constraint " + constraint.name + " is inconclusive:", 1);
                for (SimpleBound bound : constraint.simpleBounds) {
                    if (bound.isInconclusive()) {
                        log("Simple bound " + bound + " is inconclusive with valuation:", 2);
                        log(bound.x.name + ": " + bound.x, 3);
                        log(bound.y.name + ": " + bound.y, 3);
                        log("k: " + bound.k, 3);
                    }
                }
            }
        }


        return State.DECISION;
    }

    public State decision() throws InvalidVariables, InvalidVariableCreation {
        log("Decision step");
        var splitVariable = formula.variables.getSplitVariable();

        int diff = Math.abs(splitVariable.max - splitVariable.min);
        Valuation lowerHalf;
        Valuation upperHalf;
        if (diff == 1) {
            lowerHalf = splitVariable.valuation(splitVariable.min, splitVariable.min);
            upperHalf = splitVariable.valuation(splitVariable.max, splitVariable.max);
        } else {
            int half = splitRange(splitVariable.min, splitVariable.max);

            lowerHalf = splitVariable.valuation(splitVariable.min, half);
            upperHalf = splitVariable.valuation(half + 1, splitVariable.max);
        }
        log("Splitting variable " + splitVariable.name + " into " + lowerHalf + " and " + upperHalf);
        backtrackAlternatives.push(upperHalf);
        log("Opening decision level " + getDecisionLevel());
        lowerHalf.activate();

        return State.CONSISTENCY_CHECK;
    }

    public State backtrack() {
        log("Backtracking");

        if (backtrackAlternatives.empty()) {
            log("No backtrack alternatives available, formula " + formula.name + " is not satisfiable.");
            return State.NOT_SATISFIABLE;
        }

        var previousValuation = backtrackAlternatives.pop();
        log("Returning to decision level " + getDecisionLevel());

        previousValuation.activate();

        return State.CONSISTENCY_CHECK;
    }

    private void log(String message) {
        System.out.println(message);
    }

    private void log(String message, int indent) {
        System.out.println("\t".repeat(indent) + message);
    }

    public int splitRange(int min, int max) {
        return (max + min) / 2;
    }
}
