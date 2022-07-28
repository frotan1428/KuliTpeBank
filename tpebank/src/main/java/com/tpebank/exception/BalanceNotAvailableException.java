package com.tpebank.exception;


public class BalanceNotAvailableException extends RuntimeException {
    public BalanceNotAvailableException(String message){
        super(message);
    }
}
