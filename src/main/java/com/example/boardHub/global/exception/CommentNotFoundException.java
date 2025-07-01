package com.example.boardHub.global.exception;

public class CommentNotFoundException extends RuntimeException {

    public CommentNotFoundException() {

    }

    public CommentNotFoundException(String message) {
        super(message);
    }

    public CommentNotFoundException(Throwable cause) {
        super(cause);
    }

    public CommentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    protected CommentNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
