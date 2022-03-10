package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.Exeptions.AccountNotFoundException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

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
        else throw new AccountNotFoundException();

    }

    @Override
    public Account getAccountByUserId(Long userId) throws AccountNotFoundException {
        String sql="SELECT account_id,user_id,username,balance FROM account JOIN tenmo_user USING(user_id) WHERE user_id=?";
        SqlRowSet result=jdbcTemplate.queryForRowSet(sql,userId);
        if(result.next()){
            return mapRowToAccount(result);
        } else throw new AccountNotFoundException();
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
