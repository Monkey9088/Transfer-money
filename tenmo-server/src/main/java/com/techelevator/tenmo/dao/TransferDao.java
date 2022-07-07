package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {
    void transfer(Transfer transfer);
    void transferApprove(Transfer transfer);
    void transferReject(Transfer transfer);
    List<Transfer> listAllTransfersByAccountId(int accountId);
    Transfer getByTransferId(int transferId);

}
