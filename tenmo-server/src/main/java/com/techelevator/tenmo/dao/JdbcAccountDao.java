package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.Exeptions.AccountNotFoundException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao {
    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private JdbcTemplate jdbcTemplate;


    @Override
    public Account getAccountByUserName (String accountHolderName) throws AccountNotFoundException {
        String sql="SELECT account_id,user_id,username,balance FROM account JOIN tenmo_user USING(user_id) WHERE username=?";
        SqlRowSet result= jdbcTemplate.queryForRowSet(sql,accountHolderName);
        if(result.next()){
            return mapRowToAccount(result);
        }
        throw new AccountNotFoundException();

    }

    @Override
    public Account getAccountByUserId(Long userId) throws AccountNotFoundException {
        String sql="SELECT account_id,user_id,username,balance FROM account JOIN tenmo_user USING(user_id) WHERE user_id=?";
        SqlRowSet result=jdbcTemplate.queryForRowSet(sql,userId);
        if(result.next()){
            return mapRowToAccount(result);
        }
        throw new AccountNotFoundException();
    }
    public String getUserNameByAccountId(Long accountId) throws AccountNotFoundException {


        String sql="SELECT account_id,user_id,username,balance FROM account JOIN tenmo_user USING(user_id) WHERE account_id=?";
        return jdbcTemplate.queryForObject(sql,String.class,accountId);
    }

    @Override
    public Account getAccountByAccountId(Long accountId) throws AccountNotFoundException {
        String sql="SELECT account_id,user_id,username,balance FROM account JOIN tenmo_user USING(user_id) WHERE account_id=?";
        SqlRowSet result=jdbcTemplate.queryForRowSet(sql,accountId);
        if(result.next()){
            return mapRowToAccount(result);
        }
        throw new AccountNotFoundException();
    }

    @Override
    public boolean updateBalance(Long accountId,BigDecimal amount) throws AccountNotFoundException {
        boolean balanceUpdated=false;
        String sql="UPDATE account SET balance=? WHERE account_id=?";
        balanceUpdated= jdbcTemplate.update(sql,amount,accountId)==1;
        if(!balanceUpdated){
            throw new AccountNotFoundException();
        }
        return balanceUpdated;
    }


    private Account mapRowToAccount (SqlRowSet row){
        User user=new User();
        user.setId(row.getLong("user_id"));
        user.setUsername(row.getString("username"));
        Account account =new Account();
        account.setAccountUser(user);
        account.setAccountId(row.getLong("account_id"));
        account.setBalance(row.getBigDecimal("balance"));
        return account;

    }
}
