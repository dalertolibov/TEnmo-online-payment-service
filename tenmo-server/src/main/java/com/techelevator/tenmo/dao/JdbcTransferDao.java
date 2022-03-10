package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class JdbcTransferDao implements TransferDao{
    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Transfer> allTransfers(String userName) {
        List<Transfer> transfers=new ArrayList<>();
        String sql="SELECT * FROM transfer WHERE " +
                "account_from=(SELECT account_id FROM tenmo_user JOIN account USING (user_id) WHERE username=?) " +
                "OR account_to=(SELECT account_id FROM tenmo_user JOIN account USING (user_id) WHERE username=?)";
        SqlRowSet results=jdbcTemplate.queryForRowSet(sql,userName,userName);
        while(results.next()){
            transfers.add(mapRowToTransfer(results));
        }
        return transfers;
    }

    @Override
    public Transfer createTransfer(Transfer transfer, String userName) {
        String sql="INSERT INTO transfer VALUES (DEFAULT,?,?,?,?,?) RETURNING transfer_id";
        Long newTransferId=jdbcTemplate.queryForObject(sql,Long.class,transfer.getTransferTypeId(),transfer.getTransferStatusId(),transfer.getFromAccountId(),
                transfer.getToAccountId(),transfer.getToAccountId());
        return getTransfer(newTransferId,userName);
    }

    @Override
    public Transfer updateTransfer(Long transferId,Transfer transfer, String userName) {
        String sql="UPDATE transfer SET transfer_type_id=?,transfer_status_id=?, account_from=?,account_to=?,amount=? " +
                "WHERE transfer_id=? AND " +
                "(account_from=(SELECT account_id FROM tenmo_user JOIN account USING (user_id) WHERE username=?)" +
                "OR(account_to=(SELECT account_id FROM tenmo_user JOIN account USING (user_id) WHERE username=?)))";
        jdbcTemplate.update(sql,transfer.getTransferTypeId(),transfer.getTransferStatusId(),transfer.getFromAccountId(),
                transfer.getToAccountId(),transfer.getAmount(),transferId,userName,userName);
        return getTransfer(transferId,userName);

    }

    @Override
    public Transfer getTransfer(Long transferId, String userName) {
        Transfer transfer=null;
        String sql="SELECT * FROM transfer WHERE transfer_id=? AND " +
                "(account_from=(SELECT account_id FROM tenmo_user JOIN account USING (user_id) WHERE username=?) " +
                "OR(account_to=(SELECT account_id FROM tenmo_user JOIN account USING (user_id) WHERE username=?)));";
        SqlRowSet result=jdbcTemplate.queryForRowSet(sql,transferId,userName,userName);
        if(result.next()){
            transfer=mapRowToTransfer(result);
        }
        return transfer;

    }

    private Transfer mapRowToTransfer (SqlRowSet row){
        Transfer transfer=new Transfer();
        transfer.setTransferId(row.getLong("transfer_id"));
        transfer.setTransferStatusId(row.getLong("transfer_status_id"));
        transfer.setTransferTypeId(row.getLong("transfer_type_id"));
        transfer.setFromAccountId(row.getLong("account_from"));
        transfer.setToAccountId(row.getLong("account_to"));
        transfer.setAmount(row.getBigDecimal("amount"));
        return transfer;
    }

}
