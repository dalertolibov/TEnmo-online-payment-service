package com.techelevator.tenmo.Exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code= HttpStatus.BAD_REQUEST,reason = "Insufficient Funds")
public class InsufficientFundException extends Exception{

    public InsufficientFundException(String message) {
        super(message);
    }
}
