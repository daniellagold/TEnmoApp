package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exceptions.UserDoesNotExist;
import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDao {
   boolean createAccount(int userId, BigDecimal balance);
   BigDecimal getBalance (int userId);
   void updateBalance (int userId, BigDecimal newBalance);
   int getAccountIdFromUsername(String username) throws UserDoesNotExist;
   BigDecimal getBalanceByAccountId(int accountId);


}
