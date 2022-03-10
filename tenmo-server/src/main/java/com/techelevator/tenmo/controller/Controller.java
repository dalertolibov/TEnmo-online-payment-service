package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.Exeptions.AccountNotFoundException;
import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")

public class Controller {
    private UserDao userDao;
    private AccountDao accountDao;
    private TransferDao transferDao;

    public Controller(UserDao userDao, AccountDao accountDao, TransferDao transferDao) {
        this.userDao = userDao;
        this.accountDao = accountDao;
        this.transferDao = transferDao;
    }

    @GetMapping("/users")
    public List<User> allUsers(Principal principal){
       return  userDao.findAll(principal.getName());
    }


    @GetMapping ("/balance")
    public BigDecimal getBalance(Principal principal) throws AccountNotFoundException {
        return accountDao.getAccountByUserName(principal.getName()).getBalance();
    }
    @GetMapping("transfers")
    public List<Transfer>allTransfers(Principal principal){
        return transferDao.allTransfers(principal.getName());
    }





}
