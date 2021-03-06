package de.pburke;

import de.pburke.exceptions.BaseException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CspSolverTest {
    CspSolver solver;

    @Before
    public void setUp() {
        solver = new CspSolver();
    }

    @Test
    public void solve1() throws BaseException {
        var x_0 = new Variable("x_0", 0, 0);
        var x_1 = new Variable("x_1", 3, 6);
        var x_2 = new Variable("x_2", -6, 4);
        var x_3 = new Variable("x_3", -2, 5);

        //noinspection SuspiciousNameCombination
        var formula = new Formula("f_1", new Constraint[]{
            new Constraint("c_1", new SimpleBound[]{
                // x 1 >= x 0 + −1 v x 0 >= x 1 + 3 v x 2 >= x 1 + 3 v x 3 >= x 2 + −1;
                new SimpleBound(x_1, x_0, -1), new SimpleBound(x_0, x_1, 3), new SimpleBound(x_2, x_1, 3), new SimpleBound(x_3, x_2, -1)
            }),
            new Constraint("c_2", new SimpleBound[]{
                // x 1 >= x 0 + −4 v x 0 >= x 2 + 6 v x 3 >= x 2 + 4 v −10 >= 4;
                new SimpleBound(x_1, x_0, -4), new SimpleBound(x_0, x_2, 6), new SimpleBound(x_3, x_2, 4), new SimpleBound(-10, 4)
            }),
            new Constraint("c_3", new SimpleBound[]{
                // x 1 >= x 0 + −2 v x 0 >= x 3 + 6 v x 3 >= x 1 + 0 v 4 >= −10;
                new SimpleBound(x_1, x_0, -1), new SimpleBound(x_0, x_3, 6), new SimpleBound(x_3, x_1, 0), new SimpleBound(4, -10)
            })
        });

        // Assign variables separately for easier retrieval during decision step
        // and to use the same order as described in the assignment.
        solver.variables = new Variables(x_0, x_1, x_2, x_3);

        solver.formula = formula;

        var result = solver.start();

        assertEquals(result, CspSolver.Result.SATISFIABLE);
    }

    @Test
    public void solve2() throws BaseException {
        var x_0 = new Variable(0, 0);
        var x_1 = new Variable(3, 6);
        var x_2 = new Variable(-6, 4);
        var x_3 = new Variable(-2, 5);

        //noinspection SuspiciousNameCombination
        var formula = new Formula(new Constraint[]{
            new Constraint(new SimpleBound[]{
                // x 1 >= x 0 + −1 v x 0 >= x 1 + 3 v x 2 >= x 1 + 3 v x 3 >= x 2 + −1;
                new SimpleBound(x_1, x_0, -1), new SimpleBound(x_0, x_1, 3), new SimpleBound(x_2, x_1, 3), new SimpleBound(x_3, x_2, -1)
            })
        });

        // Assign variables separately for easier retrieval during decision step
        // and to use the same order as described in the assignment.
        solver.variables = new Variables(x_0, x_1, x_2, x_3);

        solver.formula = formula;

        var result = solver.start();

        assertEquals(result, CspSolver.Result.SATISFIABLE);
    }

    @Test
    public void solve3() throws BaseException {
        var x_0 = new Variable(0, 0);
        var x_1 = new Variable(3, 6);
        var x_2 = new Variable(-6, 4);
        var x_3 = new Variable(-2, 5);

        //noinspection SuspiciousNameCombination
        var formula = new Formula(new Constraint[]{
            new Constraint(new SimpleBound[]{
                // x 1 >= x 0 + −4 v x 0 >= x 2 + 6 v x 3 >= x 2 + 4 v −10 >= 4;
                new SimpleBound(x_1, x_0, -4), new SimpleBound(x_0, x_2, 6), new SimpleBound(x_3, x_2, 4), new SimpleBound(-10, 4)
            })
        });

        // Assign variables separately for easier retrieval during decision step
        // and to use the same order as described in the assignment.
        solver.variables = new Variables(x_0, x_1, x_2, x_3);

        solver.formula = formula;

        var result = solver.start();

        assertEquals(result, CspSolver.Result.SATISFIABLE);
    }

    @Test
    public void solve4() throws BaseException {
        var x_0 = new Variable(0, 0);
        var x_1 = new Variable(3, 6);
        var x_2 = new Variable(-6, 4);
        var x_3 = new Variable(-2, 5);

        //noinspection SuspiciousNameCombination
        var formula = new Formula(new Constraint[]{
            new Constraint(new SimpleBound[]{
                // x 1 >= x 0 + −2 v x 0 >= x 3 + 6 v x 3 >= x 1 + 0 v 4 >= −10;
                new SimpleBound(x_1, x_0, -1), new SimpleBound(x_0, x_3, 6), new SimpleBound(x_3, x_1, 0), new SimpleBound(4, -10)
            })
        });

        // Assign variables separately for easier retrieval during decision step
        // and to use the same order as described in the assignment.
        solver.variables = new Variables(x_0, x_1, x_2, x_3);

        solver.formula = formula;

        var result = solver.start();

        assertEquals(result, CspSolver.Result.SATISFIABLE);
    }

    @Test
    public void solve5() throws BaseException {
        SimpleBound simpleBound = new SimpleBound(4, 10);
        solver.formula = new Formula(new Constraint[]{
            new Constraint(new SimpleBound[]{
                    simpleBound
            })
        });

        solver.variables = new Variables(simpleBound.x, simpleBound.y);
        var result = solver.start();

        assertEquals(result, CspSolver.Result.NOT_SATISFIABLE);
    }

    @Test
    public void solve6() throws BaseException {
        var x_0 = new Variable("x_0", 0, 0);
        var x_1 = new Variable("x_1", 1, 6);
        var x_2 = new Variable("x_2", -2, 3);

        var simpleBound1 = new SimpleBound(x_1, x_0, 5);
        var simpleBound2 = new SimpleBound(x_2, x_1, -5);

        var constraint1 = new Constraint("c_1", new SimpleBound[] {
            // c1 =(x1 ≥x0 +5 ∨ x2 ≥x1 +(−5))
            simpleBound1, simpleBound2
        });

        var simpleBound3 = new SimpleBound(x_0, x_2, 2);
        var simpleBound4 = new SimpleBound(x_1, x_2, 4);


        var constraint2 = new Constraint("c_2", new SimpleBound[]{
            // c2 = (x0 ≥ x2 + 2 ∨ x1 ≥ x2 + 4)
            simpleBound3, simpleBound4
        });

        //noinspection SuspiciousNameCombination
        var formula = new Formula("f_1", new Constraint[]{
            constraint1, constraint2
        });

        // Assign variables separately for easier retrieval during decision step
        // and to use the same order as described in the assignment.
        solver.variables = new Variables(x_0, x_1, x_2);

        solver.formula = formula;

        var result = solver.start();

        assertEquals(result, CspSolver.Result.SATISFIABLE);
        /*fail("The formula is satisfiable but no the way the solver currently detects it! Build listeners into the" +
                "solver to check every move and make sure the steps the solver takes are exactly as in the solution in" +
                "the assignment");*/
    }
}