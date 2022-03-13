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
        Long senderAccountId=transfer.getSender().getAccountId();
        Long receiverAccountId=transfer.getReceiver().getAccountId();
        BigDecimal transferAmount= transfer.getAmount();
        Account senderAccount=accountDao.getAccountByAccountId(senderAccountId);
        Account receiverAccount=accountDao.getAccountByAccountId(receiverAccountId);
        BigDecimal senderBalance= senderAccount.getBalance();
        BigDecimal receiverBalance=receiverAccount.getBalance();
        Transfer createdTransfer=null;



        if(transfer.getTransferId()!=null
                && transfer.getStatus().getTransferStatus().equals("Approved")
                && transfer.getType().getTransferType().equals("Request")
                && senderAccount.getAccountUser().getUsername().equals(userName)){
            accountDao.updateBalance(senderAccountId,senderBalance.subtract(transferAmount));
            accountDao.updateBalance(receiverAccountId,receiverBalance.add(transferAmount));
            transfer.setSender(senderAccount);
            transfer.setReceiver(receiverAccount);
            transfer.setType(transferTypeDao.getTransferType("Request"));
            transfer.setStatus(transferStatusDao.getTransferStatus("Approved"));
            createdTransfer=transferDao.updateTransfer(transfer.getTransferId(),transfer,userName);

        }else if(transfer.getTransferId()!=null
                && transfer.getStatus().getTransferStatus().equals("Rejected")
                && transfer.getType().getTransferType().equals("Request")
                && senderAccount.getAccountUser().getUsername().equals(userName)){
            accountDao.updateBalance(senderAccountId,senderBalance.subtract(transferAmount));
            accountDao.updateBalance(receiverAccountId,receiverBalance.add(transferAmount));
            transfer.setSender(senderAccount);
            transfer.setReceiver(receiverAccount);
            transfer.setType(transferTypeDao.getTransferType("Request"));
            transfer.setStatus(transferStatusDao.getTransferStatus("Rejected"));
            createdTransfer=transferDao.updateTransfer(transfer.getTransferId(),transfer,userName);

        }
         else if (senderBalance.compareTo(transferAmount) > 0 && senderAccount.getAccountUser().getUsername().equals(userName)){

             accountDao.updateBalance(senderAccountId,senderBalance.subtract(transferAmount));
             accountDao.updateBalance(receiverAccountId,receiverBalance.add(transferAmount));
             transfer.setSender(senderAccount);
             transfer.setReceiver(receiverAccount);
             transfer.setType(transferTypeDao.getTransferType("Send"));
             transfer.setStatus(transferStatusDao.getTransferStatus("Approved"));
             createdTransfer=transferDao.createTransfer(transfer,userName);
         }
          else if( receiverAccount.getAccountUser().getUsername().equals(userName))
          {
              transfer.setSender(senderAccount);
              transfer.setReceiver(receiverAccount);
              transfer.setType(transferTypeDao.getTransferType("Request"));
              transfer.setStatus(transferStatusDao.getTransferStatus("Pending"));
              createdTransfer=transferDao.createTransfer(transfer,userName);

        }
          else {
             throw new InsufficientFundException("You don't have enough TEmoney");
         }
         return createdTransfer;



    }
}
