package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class JdbcTransferStatusDao implements TransferStatusDao{
    private JdbcTemplate jdbcTemplate;

    public JdbcTransferStatusDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public TransferStatus getTransferStatus(String status) {
        String sql="SELECT transfer_status_id,transfer_status_desc FROM transfer_status WHERE transfer_status_desc=?";
        SqlRowSet result=jdbcTemplate.queryForRowSet(sql,status);
        TransferStatus transferStatus=null;
        if(result.next()){
             transferStatus=new TransferStatus();
            transferStatus.setTransferStatus(result.getString("transfer_status_desc"));
            transferStatus.setTransferStatusId(result.getLong("transfer_status_id"));
        }return transferStatus;
    }

    @Override
    public TransferStatus getTransferStatusById(Long statusId) {
        String sql="SELECT transfer_status_id,transfer_status_desc FROM transfer_status WHERE transfer_status_id=?";
        SqlRowSet result=jdbcTemplate.queryForRowSet(sql,statusId);
        TransferStatus transferStatus=null;
        if(result.next()){
            transferStatus=new TransferStatus();
            transferStatus.setTransferStatus(result.getString("transfer_status_desc"));
            transferStatus.setTransferStatusId(result.getLong("transfer_status_id"));
        }return transferStatus;

    }
}
