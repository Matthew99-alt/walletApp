package com.walletApp.exception;

public class OperationTypeNotFoundException extends RuntimeException {
    public OperationTypeNotFoundException(String message) {
        super(message);
    }
}
