package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Account {
    private Long accountId;
    private User accountUser;
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
