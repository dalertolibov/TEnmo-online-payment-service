package com.techelevator.tenmo.service;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TransferService {
    private TransferDao transferDao;
    private UserDao userDao;
    private AccountDao accountDao;

    public TransferService(TransferDao transferDao, UserDao userDao, AccountDao accountDao) {
        this.transferDao = transferDao;
        this.userDao = userDao;
        this.accountDao = accountDao;
    }
}
