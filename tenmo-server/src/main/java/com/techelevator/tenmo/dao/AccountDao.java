package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.Exeptions.AccountNotFoundException;
import com.techelevator.tenmo.model.Account;

public interface AccountDao {
    Account getAccountByUserName (String accountHolderName) throws AccountNotFoundException;
    Account getAccountByUserId(Long userId) throws AccountNotFoundException;


}
