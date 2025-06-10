package com.eduardo.currencyexchange.exception;

public class InvalidApiResponseException extends RuntimeException {
    public InvalidApiResponseException(String message) {
        super(message);
    }

    public InvalidApiResponseException(String message, Throwable cause) {
        super(message, cause);
    }
}