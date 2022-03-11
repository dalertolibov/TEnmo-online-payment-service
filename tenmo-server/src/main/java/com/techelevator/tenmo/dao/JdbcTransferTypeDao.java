package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component

public class JdbcTransferTypeDao implements TransferTypeDao{
    JdbcTemplate jdbcTemplate;

    public JdbcTransferTypeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public TransferType getTransferType(String transferType) {
        TransferType type=null;
        String sql="SELECT transfer_type_id,transfer_type_desc FROM transfer_type WHERE transfer_type_desc=?";
        SqlRowSet results= jdbcTemplate.queryForRowSet(sql,transferType);
        if(results.next()){
            type=new TransferType();
            type.setTransferTypeId(results.getLong("transfer_type_id"));
            type.setTransferType(results.getString("transfer_type_desc"));
        }
        return type;
    }

    @Override
    public TransferType getTransferTypeById(Long transferTypeId) {
        TransferType type=null;
        String sql="SELECT transfer_type_id,transfer_type_desc FROM transfer_type WHERE transfer_type_id=?";
        SqlRowSet results= jdbcTemplate.queryForRowSet(sql,transferTypeId);
        if(results.next()){
            type=new TransferType();
            type.setTransferTypeId(results.getLong("transfer_type_id"));
            type.setTransferType(results.getString("transfer_type_desc"));
        }
        return type;

    }

}
