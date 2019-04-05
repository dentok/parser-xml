package com.dentok.parserxml.exception;

public class InputFileException extends RuntimeException {
    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message. Extends super class RuntimeException. The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public InputFileException() {
    }

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. Return code 404 with message.
     */
    public InputFileException(String message) {
        super(message);
    }
}
