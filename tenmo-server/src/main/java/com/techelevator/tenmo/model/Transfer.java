package com.techelevator.tenmo.model;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

public class Transfer {
    @NotBlank
    @Min(value = 1L, message = "TransferId must be positive")
    private Long transferId;

    @NotBlank
    @Range(min = 1L,max=2L, message = "transferTypeId must be 1 or 2")
    private Long transferTypeId;
    @NotBlank
    @Range(min = 1L,max=3L, message = "transferTypeId must be between 1 and 3")
    private Long transferStatusId;
    @NotBlank
    private Account  accountFrom;
    @NotBlank
    private Account accountTo;
    @NotBlank
    @Min(value = 0, message = "Balance can't be negative")
    private BigDecimal amount;

    public Long getTransferTypeId() {
        return transferTypeId;
    }

    public Long getTransferStatusId() {
        return transferStatusId;
    }

    public Long getTransferId() {
        return transferId;
    }

    public void setTransferId(Long transferId) {
        this.transferId = transferId;
    }


    public void setTransferTypeId(Long transferTypeId) {
        this.transferTypeId = transferTypeId;
    }


    public void setTransferStatusId(Long transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public Account getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(Account accountFrom) {
        this.accountFrom = accountFrom;
    }

    public Account getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(Account accountTo) {
        this.accountTo = accountTo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
