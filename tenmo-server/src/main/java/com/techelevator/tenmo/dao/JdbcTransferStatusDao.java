package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
@Component
public class JdbcTransferStatusDao implements TransferStatusDao{
    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferStatusDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }




    private TransferStatus mapRowToTransferStatus(SqlRowSet rs) {
        TransferStatus transferStatus = new TransferStatus();
        transferStatus.setTransferStatusId(rs.getInt("transfer_status_id"));
        transferStatus.setTransferStatusDesc(rs.getString("transfer_status_desc"));
        return transferStatus;
    }

    @Override
    public TransferStatus getTransferStatusById(int transferStatusId) {
        TransferStatus transferStatus = new TransferStatus();

        String sql = "Select * FROM transfer_status WHERE transfer_status_id = ?; ";

        SqlRowSet result = jdbcTemplate.queryForRowSet(sql,transferStatusId);

        if(result.next()){
            transferStatus = mapRowToTransferStatus(result);
        }
        return transferStatus;
    }
}
