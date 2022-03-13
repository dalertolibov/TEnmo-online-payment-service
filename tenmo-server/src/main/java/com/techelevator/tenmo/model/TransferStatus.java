package com.techelevator.tenmo.model;

import org.springframework.stereotype.Component;

@Component
public class TransferStatus {
    private Long transferStatusId;
    private String TransferStatus;

    public Long getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(Long transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public String getTransferStatus() {
        return TransferStatus;
    }

    public void setTransferStatus(String transferStatus) {
        TransferStatus = transferStatus;
    }
}
