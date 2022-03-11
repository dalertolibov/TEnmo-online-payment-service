package com.techelevator.tenmo.model;

import org.springframework.stereotype.Component;

@Component
public class TransferType {
    private Long transferTypeId;
    private String transferType;

    public Long getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(Long transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }
}
