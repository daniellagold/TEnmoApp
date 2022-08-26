package com.techelevator.tenmo.model;


import java.math.BigDecimal;

public class Transfer {
    private int transferId;
    private int accountTo;
    private int accountFrom;
    private BigDecimal amount;
    private String transferStatus;
    private String transferType;

    public Transfer(int transferId, int accountTo, int accountFrom, BigDecimal amount, String transferStatus, String transferType) {
        this.transferId = transferId;
        this.accountTo = accountTo;
        this.accountFrom = accountFrom;
        this.amount = amount;
        this.transferStatus = transferStatus;
        this.transferType = transferType;
    }

    public Transfer(){};

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public int getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(int accountTo) {
        this.accountTo = accountTo;
    }

    public int getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(int accountFrom) {
        this.accountFrom = accountFrom;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "transferId=" + transferId +
                ", accountTo=" + accountTo +
                ", accountFrom=" + accountFrom +
                ", amount=" + amount +
                ", transferStatus='" + transferStatus + '\'' +
                ", transferType='" + transferType + '\'' +
                '}';
    }
}
