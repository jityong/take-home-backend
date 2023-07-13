package com.assignment.exceptions;

public class InvalidBaseException extends Exception {
    public InvalidBaseException(final String inputBase) {
        super("Invalid base input: " + inputBase);
    }
}
