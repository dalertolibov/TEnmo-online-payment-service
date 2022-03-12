package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.Exeptions.AccountNotFoundException;
import com.techelevator.tenmo.Exeptions.InsufficientFundException;
import com.techelevator.tenmo.Exeptions.TransferNotFoundException;
import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.service.TransferService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    @GetMapping("transfers/{transferId}")
    public Transfer getTransferById(@PathVariable() Long transferId,Principal principal) throws TransferNotFoundException, AccountNotFoundException {
       return  transferDao.getTransfer(transferId, principal.getName());
    }


    @PostMapping("transfers")
    @ResponseStatus(HttpStatus.CREATED)
    public Transfer createTransfer( @RequestBody Transfer newTransfer, Principal principal) throws AccountNotFoundException, TransferNotFoundException, InsufficientFundException {


        return transferService.createTransfer(newTransfer, principal.getName());
    }
     // Probably not needed
    @PostMapping("transfers/{id}")
    public Transfer updateTransfer(@Valid @PathVariable(name="id") Long transferId,@RequestBody Transfer transfer,Principal principal) throws AccountNotFoundException, TransferNotFoundException {
        return transferDao.updateTransfer(transferId,transfer, principal.getName());
    }

    @GetMapping("accounts/{id}")
    public Account account(@PathVariable("id") Long userId) throws AccountNotFoundException {
        return accountDao.getAccountByUserId(userId);
    }
}
