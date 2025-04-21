package com.stp.exception;
public class InvalidIsoMessageException extends RuntimeException {
    public InvalidIsoMessageException(String message) {
        super(message);
    }

    public InvalidIsoMessageException(String message, Throwable cause) {
        super(message, cause);
    }
}
