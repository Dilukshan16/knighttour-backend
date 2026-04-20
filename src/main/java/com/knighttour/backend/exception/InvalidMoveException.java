package com.knighttour.backend.exception;

// ---- Custom Exceptions ----

public class InvalidMoveException extends RuntimeException {
    public InvalidMoveException(String message) { super(message); }
}

