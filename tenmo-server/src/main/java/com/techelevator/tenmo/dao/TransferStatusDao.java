package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferStatus;

public interface TransferStatusDao {

    TransferStatus getTransferStatusById(int transferStatusId);

}
