package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDao {
   boolean createAccount(int userId, BigDecimal balance);
   Account getAccount (int userId);
   BigDecimal getBalance (int accountId);


}
