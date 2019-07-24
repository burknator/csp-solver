package de.pburke;


public class Main {

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
        // and to use the same order as described in the assignment.
        f.variables = new Variables(x_0, x_1, x_2, x_3);

        var solver = new CspSolver(f);
        try {
            solver.start(f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
