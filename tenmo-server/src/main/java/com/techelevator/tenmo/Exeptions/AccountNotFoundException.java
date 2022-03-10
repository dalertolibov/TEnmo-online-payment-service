package com.techelevator.tenmo.Exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus( code = HttpStatus.NOT_FOUND, reason = "Account not found")
public class AccountNotFoundException extends Exception {
    public AccountNotFoundException(){
        super(("Account not found"));
    }
}




