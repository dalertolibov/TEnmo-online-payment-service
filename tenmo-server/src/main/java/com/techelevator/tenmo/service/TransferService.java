package com.techelevator.tenmo.service;

import com.techelevator.tenmo.Exeptions.AccountNotFoundException;
import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
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
    Transfer createTransfer (Long senderId, Long receiverId, BigDecimal transferAmount) throws AccountNotFoundException {
        Transfer transfer=new Transfer();
        Account senderAccount=new Account();
        Account receiverAccount=new Account();
        senderAccount=accountDao.getAccountByUserId(senderId);
        receiverAccount=accountDao.getAccountByUserId(receiverId);
        senderAccount.getBalance().subtract(transferAmount);
        receiverAccount.getBalance().add(transferAmount);

        transfer.setAccountTo(receiverAccount);
        transfer.setAccountFrom(senderAccount);
        transfer.setAmount(transferAmount);
        transfer.setTransferTypeId(1L);
        transfer.setTransferStatusId(1L);
        return transfer;

    }
}
