package com.techelevator.tenmo.model;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

public class Transfer {
    private int transferId;
    private int transferStatusId;
    private int transferTypeId;
    @NotEmpty
    private int transferAccountFrom;
    @NotEmpty
    private int transferAccountTo;
    @DecimalMin(value = "1", message = "You can't send negative or zero!")
    private BigDecimal amount;

    public Transfer(int transferId, int transferStatusId, int transferTypeId, int transferAccountFrom, int transferAccountTo, BigDecimal amount) {
        this.transferId = transferId;
        this.transferStatusId = transferStatusId;
        this.transferTypeId = transferTypeId;
        this.transferAccountFrom = transferAccountFrom;
        this.transferAccountTo = transferAccountTo;
        this.amount = amount;
    }

    public Transfer() {

    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public int getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(int transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public int getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(int transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public int getTransferAccountFrom() {
        return transferAccountFrom;
    }

    public void setTransferAccountFrom(int transferAccountFrom) {
        this.transferAccountFrom = transferAccountFrom;
    }

    public int getTransferAccountTo() {
        return transferAccountTo;
    }

    public void setTransferAccountTo(int transferAccountTo) {
        this.transferAccountTo = transferAccountTo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
