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
    private static final String SEND_TYPE="Send";
    private static final String REQUEST_TYPE="Request";
    private static final String APPROVED_STATUS="Approved";
    private static final String PENDING_STATUS="Pending";
    private static final String REJECTED_STATUS="Rejected";



    public TransferService(TransferDao transferDao, UserDao userDao, AccountDao accountDao, TransferTypeDao transferTypeDao, TransferStatusDao transferStatusDao) {
        this.transferDao = transferDao;
        this.userDao = userDao;
        this.accountDao = accountDao;
        this.transferTypeDao = transferTypeDao;
        this.transferStatusDao = transferStatusDao;

    }


    public Transfer createTransfer ( Transfer transfer, String userName) throws AccountNotFoundException, TransferNotFoundException, InsufficientFundException {
        Long senderAccountIdFromClient =  transfer.getSender().getAccountId();
        Long receiverAccountIdFromClient = transfer.getReceiver().getAccountId();
        BigDecimal transferAmount = transfer.getAmount();
        Account senderAccount = accountDao.getAccountByAccountId(senderAccountIdFromClient);
        Account receiverAccount = accountDao.getAccountByAccountId(receiverAccountIdFromClient);
        BigDecimal senderBalance = senderAccount.getBalance();
        BigDecimal receiverBalance = receiverAccount.getBalance();
        String senderName = senderAccount.getAccountUser().getUsername();
        Transfer createdTransfer=null;


        if(transfer.getTransferId()!=null
                && transfer.getStatus().getTransferStatus().equals(APPROVED_STATUS)
                && transfer.getType().getTransferType().equals(REQUEST_TYPE)
                && senderName.equals(userName)){
            accountDao.updateBalance(senderAccountIdFromClient,senderBalance.subtract(transferAmount));
            accountDao.updateBalance(receiverAccountIdFromClient,receiverBalance.add(transferAmount));
           Transfer updatedTransfer=setAllTransferProperties(transfer,senderAccount,receiverAccount,REQUEST_TYPE,APPROVED_STATUS);
           createdTransfer=transferDao.updateTransfer(updatedTransfer.getTransferId(),updatedTransfer,userName);

        }else if(transfer.getTransferId()!=null
                && transfer.getStatus().getTransferStatus().equals(REJECTED_STATUS)
                && transfer.getType().getTransferType().equals(REQUEST_TYPE)
                && senderName.equals(userName)){
//            accountDao.updateBalance(senderAccountIdFromClient,senderBalance.subtract(transferAmount));
//            accountDao.updateBalance(receiverAccountIdFromClient,receiverBalance.add(transferAmount));
            Transfer updatedTransfer=setAllTransferProperties(transfer,senderAccount,receiverAccount,REQUEST_TYPE,REJECTED_STATUS);
            createdTransfer=transferDao.updateTransfer(updatedTransfer.getTransferId(),updatedTransfer,userName);

        }
         else if (senderBalance.compareTo(transferAmount) > 0 && senderName.equals(userName)){

             accountDao.updateBalance(senderAccountIdFromClient,senderBalance.subtract(transferAmount));
             accountDao.updateBalance(receiverAccountIdFromClient,receiverBalance.add(transferAmount));
            Transfer updatedTransfer=setAllTransferProperties(transfer,senderAccount,receiverAccount,SEND_TYPE,APPROVED_STATUS);
             createdTransfer=transferDao.createTransfer(updatedTransfer,userName);
         }
          else if( receiverAccount.getAccountUser().getUsername().equals(userName))
          {
              Transfer updatedTransfer=setAllTransferProperties(transfer,senderAccount,receiverAccount,REQUEST_TYPE,PENDING_STATUS);
              createdTransfer=transferDao.createTransfer(updatedTransfer,userName);

        }
          else {
             throw new InsufficientFundException("You don't have enough TEmoney");
         }
         return createdTransfer;
    }
    private Transfer setAllTransferProperties(Transfer transfer,Account senderAccount,Account receiverAccount,String transferType,String transferStatus){
        transfer.setSender(senderAccount);
        transfer.setReceiver(receiverAccount);
        transfer.setType(transferTypeDao.getTransferType(transferType));
        transfer.setStatus(transferStatusDao.getTransferStatus(transferStatus));
        return transfer;
    }
}
