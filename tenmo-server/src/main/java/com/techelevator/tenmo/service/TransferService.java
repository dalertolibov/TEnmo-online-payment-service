package com.techelevator.tenmo.service;

import com.techelevator.tenmo.Exeptions.AccountNotFoundException;
import com.techelevator.tenmo.Exeptions.TransferNotFoundException;
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
 // not completed
    public Transfer createTransfer (Transfer transfer,String userName) throws AccountNotFoundException, TransferNotFoundException {
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
              createdTransfer=transferDao.createTransfer(transfer,userName);


         }else {
             //Throw not Insufficient Fund Exception
         }
         return createdTransfer;



    }
}
