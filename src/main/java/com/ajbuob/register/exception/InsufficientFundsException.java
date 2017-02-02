package com.ajbuob.register.exception;

/**
 * Created by abuob on 1/31/17.
 */
public class InsufficientFundsException extends Exception {

    public InsufficientFundsException() {
        super();
    }

    public InsufficientFundsException(String message) {
        super(message);
    }
}
