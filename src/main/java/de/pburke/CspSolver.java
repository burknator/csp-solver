package de.pburke;

import de.pburke.exceptions.BaseException;
import de.pburke.exceptions.InvalidVariableCreation;
import de.pburke.exceptions.InvalidVariables;

import java.util.Stack;

public class CspSolver {
    public Formula formula;

    /**
     * Enable or disable the deduction of new valuations when a constraint is unit.
     */
    public boolean enableDeductionStep = false;

    /**
     * The list of variables contained in {@link #formula}.
     *
     * From the formula alone, no order of the variable can be obtained. But an order is useful when searching for a
     * split variable ({@link Variables#findSplitVariable()}) or when logging the variables' valuations.
     */
    public Variables variables;

    private Stack<Valuation> backtrackAlternatives = new Stack<>();

    /**
     * Represents the internal state of the solver.
     */
    private enum State {
        CONSISTENCY_CHECK,
        BACKTRACK,
        DECISION,
        SATISFIABLE(Result.SATISFIABLE),
        NOT_SATISFIABLE(Result.NOT_SATISFIABLE);

        public Result result;

        State() { }

        State(Result result) { this.result = result; }
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

        if (!enableDeductionStep) {
            Logger.log("Using algorithm A.");
        } else {
            Logger.log("Using algorithm B.");
        }

        Logger.log("Solving formula: " + formula);

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
                case NOT_SATISFIABLE:
                    variables.logValuation();
                    Logger.log("Completed in " + steps + " steps.");
                    return currentState.result;
                default:
                    throw new BaseException("Something horrible must've happened!");
            }
            steps++;
        }
    }

    public State consistencyCheck() throws InvalidVariableCreation {
        Logger.log("Consistency check");

        log(formula);

        if (formula.hasFalseConstraint()) {
            Logger.log("At least one constraint in formula " + formula + " is false, backtracking.");
            return State.BACKTRACK;
        } else if (formula.isTrue()) {
            return State.SATISFIABLE;
        } else if (enableDeductionStep) {
            Logger.log("Deducing new valuations");
            var narrowingSuccessful = false;
            for (Constraint constraint : formula.constraints) {
                if (!constraint.isUnit()) continue;

                Logger.log("Constraint " + constraint.name + " is unit", 1);
                Logger.increaseIndentation(2);
                for (SimpleBound bound : constraint.simpleBounds) {
                    if (bound.deduceValuation()) narrowingSuccessful = true;
                }
                Logger.decreaseIndentation(2);
            }

            if (narrowingSuccessful) {
                Logger.log("Successfully narrowed down a variable");
                return State.CONSISTENCY_CHECK;
            } else {
                Logger.log("Couldn't narrow down any variables");
                return State.DECISION;
            }
        }

        // Formula doesn't contain a false constraint and is not true either -> "go to 3"
        return State.DECISION;
    }

    public State decision() throws InvalidVariables, InvalidVariableCreation {
        Logger.log("Decision step");

        var splitVariable = variables.findSplitVariable();

        Valuation lowerHalf;
        Valuation upperHalf;
        if (Math.abs(splitVariable.max - splitVariable.min) == 1) {
            // When the distance between min and max is only 1, we split the variable into the two point intervals
            // [min, min] and [max, max].
            lowerHalf = splitVariable.valuation(splitVariable.min, splitVariable.min);
            upperHalf = splitVariable.valuation(splitVariable.max, splitVariable.max);
        } else {
            int half = (splitVariable.max + splitVariable.min) / 2;

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

    private void log(SimpleBound bound, int baseIndent) {
        var state = "";
        if (bound.isInconclusive()) state = "inconclusive";
        if (bound.isTrue()) state = "true";
        if (bound.isFalse()) state = "false";

        Logger.log("Simple bound " + bound + " is " + state + " with valuation:", baseIndent + 1);
        Logger.log(bound.x.name + ": " + bound.x, baseIndent + 2);
        Logger.log(bound.y.name + ": " + bound.y, baseIndent + 2);
        Logger.log("k: " + bound.k, baseIndent + 2);
    }

    private void log(Constraint constraint, int baseIndent) {
        var state = "";
        if (constraint.isTrue()) state = "true";
        if (constraint.isFalse()) state = "false";
        if (constraint.isInconclusive()) state = "inconclusive";

        Logger.log("Constraint " + constraint.name + " is " + state + ":", baseIndent + 1);
        for (SimpleBound bound : constraint.simpleBounds) {
            log(bound, 1);
        }
    }

    private void log(Formula formula) {
        var state = "";
        if (formula.isTrue()) state = "true";
        if (formula.isFalse()) state = "false";
        if (formula.isInconclusive()) state = "inconclusive";

        Logger.log("Formula " + formula.name + " is " + state + ":");
        for (Constraint constraint : formula.constraints) {
            log(constraint, 0);
        }
    }
}
