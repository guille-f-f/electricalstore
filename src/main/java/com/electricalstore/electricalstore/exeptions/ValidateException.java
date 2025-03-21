package com.electricalstore.electricalstore.exeptions;

public class ValidateException extends RuntimeException {
    public ValidateException(String message) {
        super(message);
    }

    public ValidateException(String message, Throwable cause) {
        super(message, cause);
    }
}
