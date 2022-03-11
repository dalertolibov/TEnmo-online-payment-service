package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.Exeptions.AccountNotFoundException;
import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDao {
    Account getAccountByUserName (String accountHolderName) throws AccountNotFoundException;
    Account getAccountByUserId(Long userId) throws AccountNotFoundException;
    String getUserNameByAccountId(Long accountId) throws AccountNotFoundException;
    Account getAccountByAccountId(Long accountId) throws AccountNotFoundException;
    boolean updateBalance (Long accountId,BigDecimal amount) throws AccountNotFoundException;


}
