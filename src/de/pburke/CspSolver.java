package de.pburke;

import java.util.Stack;

public class CspSolver {
    public Formula formula;
    private Stack<Valuation> backtrackAlternatives = new Stack<>();
    private int decisionLevel = 0;
    
    enum State {
        CONSISTENCY_CHECK, BACKTRACK, DECISION, SATISFIABLE, NOT_SATISFIABLE
    }

    public CspSolver() { }

    public CspSolver(Formula formula) {
        this.formula = formula;
    }

    public void start(Formula formula) throws Exception {
        var currentState = State.CONSISTENCY_CHECK;

        mainLoop: while (true) {
            switch (currentState) {
                case CONSISTENCY_CHECK:
                    currentState = consistencyCheck(formula);
                    break;
                case BACKTRACK:
                    currentState = backtrack();
                    break;
                case DECISION:
                    currentState = decision(formula);
                    break;
                case SATISFIABLE:
                    System.out.println("The formula is satisfiable.");
                    break mainLoop;
                case NOT_SATISFIABLE:
                    System.out.println("The formula is not satisfiable.");
                    break mainLoop;
                default:
                    break mainLoop;
            }
        }
    }

    public State consistencyCheck(Formula formula) {
        if (formula.isTrue()) return State.SATISFIABLE;
        if (formula.isFalse()) return State.BACKTRACK;

        return State.DECISION;
    }

    public State decision(Formula formula) throws Exception {
        var splitVariable = formula.variables.getSplitVariable();

        int half = (splitVariable.max - splitVariable.min) / 2;
        var lowerHalf = splitVariable.valuation(splitVariable.min, half);
        var upperHalf = splitVariable.valuation(half + 1, splitVariable.max);
        lowerHalf.activate();

        backtrackAlternatives.push(upperHalf);
        decisionLevel++;

        return State.CONSISTENCY_CHECK;
    }

    public State backtrack() {
        if (backtrackAlternatives.empty()) return State.NOT_SATISFIABLE;

        var previousValuation = backtrackAlternatives.pop();
        decisionLevel--;

        previousValuation.activate();

        return State.CONSISTENCY_CHECK;
    }
}
