package com.techelevator.tenmo.Exeptions;

public class InsufficientFundException extends Exception{

    public InsufficientFundException(String message) {
        super(message);
    }
}
