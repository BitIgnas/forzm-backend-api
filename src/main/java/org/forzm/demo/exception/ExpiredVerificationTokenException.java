package org.forzm.demo.exception;

public class ExpiredVerificationTokenException extends RuntimeException {
    public ExpiredVerificationTokenException(String message) {
        super(message);
    }
}
