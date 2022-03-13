package com.techelevator.tenmo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class Account {
    @NotBlank
    @Min(value = 1L, message = " accountId must be positive")
    private Long accountId;

    private User accountUser;
    @JsonIgnore
    private BigDecimal balance;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public User getAccountUser() {
        return accountUser;
    }

    public void setAccountUser(User accountHolder) {
        this.accountUser = accountHolder;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Account() {
    }

    public Account(Long accountId, User accountHolder, BigDecimal balance) {
        this.accountId = accountId;
        this.accountUser = accountHolder;
        this.balance = balance;

    }
}
