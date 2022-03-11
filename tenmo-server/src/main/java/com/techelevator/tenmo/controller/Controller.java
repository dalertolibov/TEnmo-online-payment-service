package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.Exeptions.AccountNotFoundException;
import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.service.TransferService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")

public class Controller {
    private UserDao userDao;
    private AccountDao accountDao;
    private TransferDao transferDao;
    private TransferService transferService;

    public Controller(UserDao userDao, AccountDao accountDao, TransferDao transferDao,TransferService transferService) {
        this.userDao = userDao;
        this.accountDao = accountDao;
        this.transferDao = transferDao;
        this.transferService=transferService;
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
    public List<Transfer>allTransfers(Principal principal) throws AccountNotFoundException {

        return transferDao.allTransfers(principal.getName());
    }


    @PostMapping("transfers")
    public Transfer createTransfer(@RequestBody Transfer newTransfer, Principal principal) throws AccountNotFoundException {


        return transferService.createTransfer(newTransfer, principal.getName());
    }

    @PostMapping("transfers/{transferId}")
    public Transfer updateTransfer(@PathVariable Long transferId,@RequestBody Transfer transfer,Principal principal) throws AccountNotFoundException {
        return transferDao.updateTransfer(transferId,transfer, principal.getName());
    }
    @GetMapping("accounts/{userId}")
    public Account account(@PathVariable Long userId) throws AccountNotFoundException {
        return accountDao.getAccountByUserId(userId);
    }






}
