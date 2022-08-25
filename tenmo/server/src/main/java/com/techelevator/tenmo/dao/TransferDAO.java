package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;

public interface TransferDAO {
    boolean subtractFrom(int userId, BigDecimal amount);
    void addTo(int userId, BigDecimal amount);
    boolean createTransfer(Transfer transfer);
}
