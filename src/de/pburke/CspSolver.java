package de.pburke;

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

    public Result start() throws Exception {
        var currentState = State.CONSISTENCY_CHECK;

        mainLoop: while (true) {
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
                    throw new Exception("Something horrible must've happened!");
            }
        }
    }

    public State consistencyCheck() {
        if (formula.isTrue()) return State.SATISFIABLE;
        if (formula.isFalse()) return State.BACKTRACK;

        return State.DECISION;
    }

    public State decision() throws Exception {
        var splitVariable = formula.variables.getSplitVariable();

        int half = (splitVariable.max - splitVariable.min + 1) / 2;
        var lowerHalf = splitVariable.valuation(splitVariable.min, half);
        var upperHalf = splitVariable.valuation(half + 1, splitVariable.max);
        lowerHalf.activate();

        backtrackAlternatives.push(upperHalf);

        return State.CONSISTENCY_CHECK;
    }

    public State backtrack() {
        if (backtrackAlternatives.empty()) return State.NOT_SATISFIABLE;

        var previousValuation = backtrackAlternatives.pop();

        previousValuation.activate();

        return State.CONSISTENCY_CHECK;
    }
}
