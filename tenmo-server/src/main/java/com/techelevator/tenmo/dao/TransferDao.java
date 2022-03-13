package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.Exeptions.AccountNotFoundException;
import com.techelevator.tenmo.Exeptions.TransferNotFoundException;
import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {
    List<Transfer> allTransfers(String userName) throws AccountNotFoundException;
    Transfer createTransfer(Transfer transfer,String userName ) throws AccountNotFoundException, TransferNotFoundException;
    Transfer updateTransfer(Long transferId,Transfer transfer,String userName) throws AccountNotFoundException, TransferNotFoundException;
    Transfer getTransfer(Long transferId,String userName) throws AccountNotFoundException, TransferNotFoundException;
}
