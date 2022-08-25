package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDAO {
    boolean subtractFrom(int userId, BigDecimal amount);
    void addTo(int userId, BigDecimal amount);
    boolean createTransfer(Transfer transfer);
    List<Transfer> getTransfersForUser(int accountId);
    Transfer getTransferByTransferId(int transferId, int accountId);
}
