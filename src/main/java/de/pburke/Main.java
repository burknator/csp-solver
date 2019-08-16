package de.pburke;


import de.pburke.exceptions.BaseException;
import java.io.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            throw new Exception("Provide an input file.");
        }

        StringBuilder input = new StringBuilder();
        Scanner scanner = new Scanner(new File (args[0]));

        while (scanner.hasNextLine()) {
            input.append(" ").append(scanner.nextLine());
        }
        scanner.close();

        var parser = new Parser();
        var f = parser.parse(input.toString());
        var solver = new CspSolver(f);

        if (args.length > 1 && args[1].equals("-b")) {
            solver.enableDeductionStep = true;
        }

        // Assign variables separately for easier retrieval during decision step
        // and to use the same order as described in the assignment.
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
