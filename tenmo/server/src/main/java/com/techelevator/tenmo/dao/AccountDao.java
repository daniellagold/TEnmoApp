package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exceptions.UserDoesNotExist;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.TransferDto;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;

public interface AccountDao {
   boolean createAccount(int userId, BigDecimal balance);
   BigDecimal getBalance (int userId);
   String updateBalance (String username, TransferDto transferDto);
   Integer getAccountIdFromUsername(String username) throws UserDoesNotExist;
   BigDecimal getBalanceByAccountId(int accountId);


}
