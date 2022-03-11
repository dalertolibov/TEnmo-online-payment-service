package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Account {
    private Long accountId;
    private User accountUser;
    //

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public User getAccountUser() {
        return accountUser;
    }

    public void setAccountUser(User accountUser) {
        this.accountUser = accountUser;
    }
//
//   // public BigDecimal getBalance() {
//        return balance;
//    }
//
//   // public void setBalance(BigDecimal balance) {
//        this.balance = balance;
//    }
}
