package io.spring.exception;

public class TenantMismatchException extends RuntimeException {

    public TenantMismatchException(String message) {
        super(message);
    }
}
