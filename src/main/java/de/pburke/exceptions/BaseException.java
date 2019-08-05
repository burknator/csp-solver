package de.pburke.exceptions;

public class BaseException extends Throwable {
    private static final long serialVersionUID = 1L;

    public BaseException(String message) {
        super(message);
    }
}
