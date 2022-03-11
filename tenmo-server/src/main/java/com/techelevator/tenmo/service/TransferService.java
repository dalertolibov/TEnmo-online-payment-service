package com.techelevator.tenmo.service;

import com.techelevator.tenmo.Exeptions.AccountNotFoundException;
import com.techelevator.tenmo.Exeptions.InsufficientFundException;
import com.techelevator.tenmo.Exeptions.TransferNotFoundException;
import com.techelevator.tenmo.dao.*;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferStatus;
import com.techelevator.tenmo.model.TransferType;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@Component
public class TransferService {
    private TransferDao transferDao;
    private UserDao userDao;
    private AccountDao accountDao;
    private TransferTypeDao transferTypeDao;
    private TransferStatusDao transferStatusDao;

    public TransferService(TransferDao transferDao, UserDao userDao, AccountDao accountDao, TransferTypeDao transferTypeDao, TransferStatusDao transferStatusDao) {
        this.transferDao = transferDao;
        this.userDao = userDao;
        this.accountDao = accountDao;
        this.transferTypeDao = transferTypeDao;
        this.transferStatusDao = transferStatusDao;
    }

    // not completed
    public Transfer createTransfer ( Transfer transfer, String userName) throws AccountNotFoundException, TransferNotFoundException, InsufficientFundException {
        Long senderAccountId=transfer.getAccountFrom().getAccountId();
        Long receiverAccountId=transfer.getAccountTo().getAccountId();

        BigDecimal transferAmount= transfer.getAmount();
        Account senderAccount=accountDao.getAccountByAccountId(senderAccountId);
        Account receiverAccount=accountDao.getAccountByAccountId(receiverAccountId);
        BigDecimal senderBalance= senderAccount.getBalance();
        BigDecimal receiverBalance=receiverAccount.getBalance();
        Transfer createdTransfer=null;
         if (senderBalance.compareTo(transferAmount)==1){

             accountDao.updateBalance(senderAccountId,senderBalance.subtract(transferAmount));
             accountDao.updateBalance(receiverAccountId,receiverBalance.add(transferAmount));
             transfer.setAccountFrom(senderAccount);
             transfer.setAccountTo(receiverAccount);
             transfer.setType(transferTypeDao.getTransferType("Send"));
             transfer.setStatus(transferStatusDao.getTransferStatus("Approved"));
             createdTransfer=transferDao.createTransfer(transfer,userName);


         }else {
             throw new InsufficientFundException("You don't have enough TEmoney");
         }
         return createdTransfer;



    }
}
