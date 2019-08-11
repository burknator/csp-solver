package de.pburke;


import de.pburke.exceptions.BaseException;
import de.pburke.exceptions.InvalidVariableCreation;
import java.io.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws InvalidVariableCreation, IOException {

        String input = "";
        Scanner scnr = new Scanner(new File (args[0]));

        while (scnr.hasNextLine()) {
            input += " " + scnr.nextLine();
        }

        Parser parser = new Parser();
        var f = parser.parse(input);
        Logger.log("Formula:" + f.toString());

        /*var x_0 = new Variable("x_0", 0, 0);
        var x_1 = new Variable("x_1", 3, 6);
        var x_2 = new Variable("x_2", -6, 4);
        var x_3 = new Variable("x_3", -2, 5);*/

        //noinspection SuspiciousNameCombination
        /*var f = new Formula(new Constraint[]{
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
        });*/

        // Assign variables separately for easier retrieval during decision step
        // and to use the same order as described in the assignment.

        var solver = new CspSolver(f);
        solver.variables = new Variables(parser.getVariableList());
        try {
            var result = solver.start();

            switch (result) {
                case SATISFIABLE:
                    System.out.println("The formula is satisfiable.");
                    break;
                case NOT_SATISFIABLE:
                    System.out.println("The formula is not satisfiable.");
                    break;
            }
        } catch (BaseException e) {
            e.printStackTrace();
        }
    }
}
