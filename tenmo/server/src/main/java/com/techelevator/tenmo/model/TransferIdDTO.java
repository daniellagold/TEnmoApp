package com.techelevator.tenmo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransferIdDTO {
    @JsonProperty("transfer_id")
    private int transferId;

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }
}
