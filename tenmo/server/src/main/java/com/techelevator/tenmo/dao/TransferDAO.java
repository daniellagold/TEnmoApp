package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDAO {
    String subtractFrom(int userId, BigDecimal amount);
    boolean addTo(int userId, BigDecimal amount);
    boolean createTransfer(Transfer transfer);
    List<Transfer> getTransfersForUser(int accountId);
    Transfer getTransferByTransferId(int transferId, int accountId);

    List<Transfer> getTransferByStatus(String transferStatus, int accountId);
}
