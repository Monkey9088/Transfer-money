package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferStatus;
import com.techelevator.tenmo.model.TransferType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class JdbcTransferTypeDao implements TransferTypeDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferTypeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }




    private TransferType mapRowToTransferType(SqlRowSet rs) {
        TransferType transferType= new TransferType();
        transferType.setTransferTypeId(rs.getInt("transfer_type_id"));
        transferType.setTransferType(rs.getString("transfer_type_desc"));
        return transferType;
    }


    @Override
    public TransferType getAllTransfersById(int transferTypeId) {
        TransferType transferType = new TransferType();
        String sql = "SELECT * FROM transfer_type WHERE transfer_type_id = ?; ";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql,transferTypeId);
        if(result.next()){
            transferType = mapRowToTransferType(result);
        }
        return transferType;
    }
}
