package org.forzm.demo.exception;

public class VerificationTokenException extends RuntimeException {
    private String message;

    public VerificationTokenException(String message) {
        this.message = message;
    }
}
