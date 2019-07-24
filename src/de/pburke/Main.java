package de.pburke;


import java.util.*;

public class Main {
    public Stack<Valuation> backtrackAlternatives = new Stack<>();
    public int decisionLevel = 0;

    enum State {
        CONSISTENCY_CHECK, BACKTRACK, DECISION, SATISFIABLE, NOT_SATISFIABLE
    }

    public static void main(String[] args) {
        var x_0 = new Variable(0, 0);
        var x_1 = new Variable(3, 6);
        var x_2 = new Variable(-6, 4);
        var x_3 = new Variable(-2, 5);

        //noinspection SuspiciousNameCombination
        var f = new Formula(new Constraint[]{
            new Constraint(new SimpleBound[]{
                // x 1 >= x 0 + −1 v x 0 >= x 1 + 3 v x 2 >= x 1 + 3 v x 3 >= x 2 + −1;
                new SimpleBound(x_1, x_0, -1), new SimpleBound(x_0, x_1, 3), new SimpleBound(x_2, x_1, 3), new SimpleBound(x_3, x_2, -1)
            }),
            new Constraint(new SimpleBound[]{
                // x 1 >= x 0 + −4 v x 0 >= x 2 + 6 v x 3 >= x 2 + 4 v −10 >= 4;
                new SimpleBound(x_1, x_0, -4), new SimpleBound(x_0, x_2, 6), new SimpleBound(x_3, x_2, 4), new SimpleBound(-10, 4)
            }),
            new Constraint(new SimpleBound[]{
                // x 1 >= x 0 + −2 v x 0 >= x 3 + 6 v x 3 >= x 1 + 0 v 4 >= −10;
                new SimpleBound(x_1, x_0, -1), new SimpleBound(x_0, x_3, 6), new SimpleBound(x_3, x_1, 0), new SimpleBound(4, -10)
            })
        });

        // Assign variables separately for easier retrieval during decision step
        f.variables = new Variables(x_0, x_1, x_2, x_3);

        var main = new Main();
        try {
            main.start(f);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
