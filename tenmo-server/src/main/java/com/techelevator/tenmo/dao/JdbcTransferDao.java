package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Balance;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao{
    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public void transfer(Transfer transfer) {
        //Getting Sender and Receiver id
        AccountDao accountDao = new JdbcAccountDao(jdbcTemplate);
        int accountFromId = transfer.getTransferAccountFrom();
        int accountToId = transfer.getTransferAccountTo();
        Account accountFrom = accountDao.getAccountById(accountFromId);
        Account accountTo = accountDao.getAccountById(accountToId);

        //Get Balance of accounts
        BigDecimal accountFromBalance = accountFrom.getBalance().getBalance();
        BigDecimal accountToBalance = accountTo.getBalance().getBalance();
        BigDecimal transferAmount = transfer.getAmount();

        //If the status is approved (transaction) -- Can be changed
        if (transfer.getTransferStatusId() == 2) {
                accountFromBalance = accountFromBalance.subtract(transferAmount);
                accountToBalance = accountToBalance.add(transferAmount);
        }

        // Insert transfer in transfer table and update balance for each sender and receiver
        String sql = "INSERT INTO tenmo_transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (? ,?, ?, ?, ? ); " +
                "UPDATE tenmo_account SET balance = ? WHERE account_id = ?; " +
                "UPDATE tenmo_account SET balance = ? WHERE account_id = ?; ";

        jdbcTemplate.update(sql,transfer.getTransferTypeId(), transfer.getTransferStatusId(),transfer.getTransferAccountFrom(),
                transfer.getTransferAccountTo(),transfer.getAmount(),accountFromBalance, accountFromId, accountToBalance, accountToId);
    }

    @Override
    public void transferApprove(Transfer transfer) {
        transfer.setTransferStatusId(2);
        transfer.setTransferTypeId(2);
        transfer(transfer);
        /*String sql= "UPDATE tenmo_transfer SET transfer_status_id = ? WHERE transfer_id = ?;";
        jdbcTemplate.update(sql,transferStatus, transfer.getTransferId());*/

    }

    @Override
    public void transferReject(Transfer transfer) {
        transfer.setTransferStatusId(3);
        transfer(transfer);
    }

    @Override
    public List<Transfer> listAllTransfersByAccountId(int accountId) {
        List<Transfer> transferList = new ArrayList<>();
        String sql = "SELECT * FROM tenmo_transfer WHERE account_to = ? OR account_from = ?; ";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql,accountId,accountId);

        while(results.next()){
            Transfer transfer = mapRowToTransfer(results);
            transferList.add(transfer);
        }

        return transferList;

    }

    @Override
    public Transfer getByTransferId(int transferId) {
        Transfer transfer = new Transfer();
        String sql = "SELECT * FROM tenmo_transfer WHERE transfer_id = ?;";

        SqlRowSet result = jdbcTemplate.queryForRowSet(sql,transferId);

        if(result.next()){
            transfer = mapRowToTransfer(result);
        }
        return transfer;
    }

    private Transfer mapRowToTransfer(SqlRowSet rs) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rs.getInt("transfer_id"));
        transfer.setTransferAccountTo(rs.getInt("account_to"));
        transfer.setTransferAccountFrom(rs.getInt("account_from"));
        transfer.setTransferStatusId(rs.getInt("transfer_status_id"));
        transfer.setAmount(new BigDecimal(rs.getString("amount")));
        transfer.setTransferTypeId(rs.getInt("transfer_type_id"));
        return transfer;
    }
}
