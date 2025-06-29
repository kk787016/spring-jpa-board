package com.example.boardHub.global.exception;

public class BoardNotFoundException extends RuntimeException {
    public BoardNotFoundException() {
    }

    public BoardNotFoundException(String message) {
        super(message);
    }

    public BoardNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public BoardNotFoundException(Throwable cause) {
        super(cause);
    }

    public BoardNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
