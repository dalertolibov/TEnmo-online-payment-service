package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {
    List<Transfer> allTransfers(String userName);
    Transfer createTransfer(Transfer transfer,String userName );
    Transfer updateTransfer(Long trasnferId,Transfer transfer,String userName);
    Transfer getTransfer(Long transferId,String userName);
}
