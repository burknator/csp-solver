package de.pburke;

public class Logger {
    private static int indentation = 0;

    private Logger () { }

    public static void increaseIndentation() {
        increaseIndentation(1);
    }

    public static void increaseIndentation(int by) {
        indentation += by;
    }

    public static void decreaseIndentation() {
        decreaseIndentation(1);
    }

    public static void decreaseIndentation(int by) {
        indentation = Math.max(0, indentation - by);
    }

    public static void log(String message, int indent) {
        print(message, indent);
    }

    public static void log(String message) {
        print(message, indentation);
    }

    private static void print(String message, int indentation) {
        System.out.println("\t".repeat(indentation) + message);
    }
}
