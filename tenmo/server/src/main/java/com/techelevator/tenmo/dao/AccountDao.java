package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.RequestDto;
import com.techelevator.tenmo.model.TransferDto;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;

public interface AccountDao {
   boolean createAccount(int userId, BigDecimal balance);
   BigDecimal getBalance (int userId);
   String updateBalance (String username, TransferDto transferDto);
   Integer getAccountIdFromUsername(String username);
   BigDecimal getBalanceByAccountId(int accountId);
   String RequestMoney(String username, RequestDto requestDto);

   void approveRequest(int transferId);

   String updateBalanceAfterRequest(int transferId, int accountId);
}
