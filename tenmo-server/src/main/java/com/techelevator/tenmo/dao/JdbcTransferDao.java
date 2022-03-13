package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.Exeptions.AccountNotFoundException;
import com.techelevator.tenmo.Exeptions.TransferNotFoundException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferStatus;
import com.techelevator.tenmo.model.TransferType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class JdbcTransferDao implements TransferDao{
    private JdbcTemplate jdbcTemplate;
    AccountDao accountDao;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate,AccountDao accountDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.accountDao=accountDao;
    }

    @Override
    public List<Transfer> allTransfers(String userName) throws AccountNotFoundException {
        List<Transfer> transfers=new ArrayList<>();
        String sql="SELECT * FROM transfer JOIN transfer_type USING (transfer_type_id) JOIN transfer_status USING (transfer_status_id) WHERE " +
                "account_from=(SELECT account_id FROM tenmo_user JOIN account USING (user_id) WHERE username=?) " +
                "OR account_to=(SELECT account_id FROM tenmo_user JOIN account USING (user_id) WHERE username=?)";
        SqlRowSet results=jdbcTemplate.queryForRowSet(sql,userName,userName);
        while(results.next()){
            transfers.add(mapRowToTransfer(results));
        }
        return transfers;
    }

    @Override
    public Transfer createTransfer(Transfer transfer, String userName) throws AccountNotFoundException, TransferNotFoundException {
       Long newTransferId=null;
        String sql="INSERT INTO transfer VALUES (DEFAULT,?,?,?,?,?) RETURNING transfer_id";
         newTransferId=jdbcTemplate.queryForObject(sql,Long.class,
                transfer.getType().getTransferTypeId(),
                transfer.getStatus().getTransferStatusId(),
                transfer.getSender().getAccountId(),
                transfer.getReceiver().getAccountId(),
                transfer.getAmount());
         if(newTransferId==null){
             throw new TransferNotFoundException();
         }
        return getTransfer(newTransferId,userName);
    }

    @Override
    public Transfer updateTransfer(Long transferId,Transfer transfer, String userName) throws TransferNotFoundException, AccountNotFoundException {
        boolean transferUpdated=false;
        String sql="UPDATE transfer SET transfer_type_id=?,transfer_status_id=?, account_from=?,account_to=?,amount=? " +
                "WHERE transfer_id=? AND " +
                "(account_from=(SELECT account_id FROM tenmo_user JOIN account USING (user_id) WHERE username=?)" +
                "OR(account_to=(SELECT account_id FROM tenmo_user JOIN account USING (user_id) WHERE username=?)))";
        transferUpdated=jdbcTemplate.update(sql,
                transfer.getType().getTransferTypeId(),
                transfer.getStatus().getTransferStatusId(),
                transfer.getSender().getAccountId(),
                transfer.getReceiver().getAccountId(),
                transfer.getAmount(),
                transferId,
                userName,userName)==1;
        if(!transferUpdated){
            throw new TransferNotFoundException();
        }
        return getTransfer(transferId,userName);

    }

    @Override
    public Transfer getTransfer(Long transferId, String userName) throws TransferNotFoundException, AccountNotFoundException {
Transfer transfer=null;
        String sql="SELECT * FROM transfer JOIN transfer_type USING (transfer_type_id) " +
                "JOIN transfer_status USING (transfer_status_id) WHERE transfer_id=? " +
                "AND (account_from=(SELECT account_id FROM tenmo_user JOIN account USING " +
                "(user_id) WHERE username=?)  OR(account_to=(SELECT account_id FROM " +
                "tenmo_user JOIN account USING (user_id) WHERE username=?)))";
        SqlRowSet result=jdbcTemplate.queryForRowSet(sql,transferId,userName,userName);

            if(result.next()){return transfer= mapRowToTransfer(result);}


        throw new TransferNotFoundException();


    }

    private Transfer mapRowToTransfer (SqlRowSet row) throws AccountNotFoundException {
        Transfer transfer=new Transfer();
        TransferStatus status=new TransferStatus();
        TransferType type=new TransferType();

        status.setTransferStatusId(row.getLong("transfer_status_id"));
        status.setTransferStatus(row.getString("transfer_status_desc"));
        type.setTransferTypeId(row.getLong("transfer_type_id"));
        type.setTransferType(row.getString("transfer_type_desc"));
        transfer.setAmount(row.getBigDecimal("amount"));
        transfer.setTransferId(row.getLong("transfer_id"));
        transfer.setStatus(status);
        transfer.setType(type);
        transfer.setSender(accountDao.getAccountByAccountId(row.getLong("account_from")));
        transfer.setReceiver(accountDao.getAccountByAccountId(row.getLong("account_to")));

        return transfer;
    }

}
