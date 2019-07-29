package de.pburke;

import de.pburke.exceptions.BaseException;
import de.pburke.exceptions.InvalidVariableCreation;
import de.pburke.exceptions.InvalidVariables;

import java.util.Stack;

public class CspSolver {
    public Formula formula;
    public boolean enableDeductionStep = true;
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
        var steps = 0;

        while (true) {
            Logger.log("-".repeat(72));
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
                    logValuation();
                    Logger.log("Completed in " + steps + " steps.");
                    return Result.SATISFIABLE;
                case NOT_SATISFIABLE:
                    logValuation();
                    Logger.log("Completed in " + steps + " steps.");
                    return Result.NOT_SATISFIABLE;
                default:
                    throw new BaseException("Something horrible must've happened!");
            }
            steps++;
        }
    }

    public State consistencyCheck() throws InvalidVariableCreation {
        Logger.log("Consistency check");
        if (formula.isTrue()) {
            Logger.log("Formula " + formula.name + " is true:");
            for (Constraint constraint : formula.constraints) {
                Logger.log("Constraint " + constraint.name + " is true:", 1);
                for (SimpleBound bound : constraint.simpleBounds) {
                    if (bound.isTrue()) {
                        Logger.log("Simple bound " + bound + " is true with valuation:", 2);
                        Logger.log(bound.x.name + ": " + bound.x, 3);
                        Logger.log(bound.y.name + ": " + bound.y, 3);
                        Logger.log("k: " + bound.k, 3);
                    }
                }
            }

            return State.SATISFIABLE;
        }

        if (formula.isFalse()) {
            Logger.log("Formula " + formula.name + " is false:");
            for (Constraint constraint : formula.constraints) {
                if (constraint.isFalse()) {
                    Logger.log("Constraint " + constraint.name + " is false:", 1);
                    for (SimpleBound bound : constraint.simpleBounds) {
                        if (bound.isFalse()) {
                            Logger.log("Simple bound " + bound + " is false with valuation:", 2);
                            Logger.log(bound.x.name + ": " + bound.x, 3);
                            Logger.log(bound.y.name + ": " + bound.y, 3);
                            Logger.log("k: " + bound.k, 3);
                        }
                    }
                }
            }
            return State.BACKTRACK;
        }

        if (enableDeductionStep) {
            Logger.log("Deducing new valuations");
            for (Constraint constraint : formula.constraints) {
                if (constraint.isUnit()) {
                    Logger.log("Constraint " + constraint.name + " is unit", 1);
                    Logger.increaseIndentation(2);
                    for (SimpleBound bound : constraint.simpleBounds) {
                        bound.calculateNewValuation();
                    }
                    Logger.decreaseIndentation(2);
                }
            }
        }

        Logger.log("Formula " + formula.name + " is inconclusive:");
        for (Constraint constraint : formula.constraints) {
            if (constraint.isInconclusive()) {
                Logger.log("Constraint " + constraint.name + " is inconclusive:", 1);
                for (SimpleBound bound : constraint.simpleBounds) {
                    if (bound.isInconclusive()) {
                        Logger.log("Simple bound " + bound + " is inconclusive with valuation:", 2);
                        Logger.log(bound.x.name + ": " + bound.x, 3);
                        Logger.log(bound.y.name + ": " + bound.y, 3);
                        Logger.log("k: " + bound.k, 3);
                    }
                }
            }
        }


        return State.DECISION;
    }

    public State decision() throws InvalidVariables, InvalidVariableCreation {
        Logger.log("Decision step");
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
        Logger.log("Splitting variable " + splitVariable.name + " into " + lowerHalf + " and " + upperHalf);
        backtrackAlternatives.push(upperHalf);
        Logger.log("Opening decision level " + getDecisionLevel());
        lowerHalf.activate();

        return State.CONSISTENCY_CHECK;
    }

    public State backtrack() {
        Logger.log("Backtracking");

        if (backtrackAlternatives.empty()) {
            Logger.log("No backtrack alternatives available, formula " + formula.name + " is not satisfiable.");
            return State.NOT_SATISFIABLE;
        }

        var previousValuation = backtrackAlternatives.pop();
        Logger.log("Returning to decision level " + getDecisionLevel());

        previousValuation.activate();

        return State.CONSISTENCY_CHECK;
    }

    private void logValuation() {
        Logger.log("Valuation:");
        for (Variable variable : formula.variables.getVariables()) {
            Logger.log(variable.name + ": " + variable, 1);
        }
    }

    public int splitRange(int min, int max) {
        return (max + min) / 2;
    }
}
