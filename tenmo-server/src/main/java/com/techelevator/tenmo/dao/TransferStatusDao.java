package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferStatus;

public interface TransferStatusDao {
    TransferStatus getTransferStatus(String status);
    TransferStatus getTransferStatusById(Long statusId);
}
