package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.security.Principal;

@Component
public class JdbcAccountDao implements AccountDao{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private JdbcUserDao jdbcUserDao;
    @Autowired
    private TransferDAO transferDAO;

    public JdbcAccountDao(){}

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public JdbcAccountDao(JdbcTemplate jdbcTemplate, TransferDAO transferDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.transferDAO = transferDAO;
    }

    public void setTransferDAO(TransferDAO transferDAO) {
        this.transferDAO = transferDAO;
    }

    @Override
    public boolean createAccount(int userId, BigDecimal balance) {
        boolean success = false;
        Integer newAccountId;
        String sql = "INSERT INTO account (user_id, balance) VALUES (?, ?) RETURNING account_id;";
       try{
           newAccountId = jdbcTemplate.queryForObject(sql, Integer.class, userId, balance);
           success = true;
       } catch (Exception e){
           success = false;
       }
       return success;
    }

    @Override
    public Integer getAccountIdFromUsername(String username) {
        Integer userId = null;
        try {
            String sql = "SELECT user_id FROM tenmo_user WHERE username ILIKE ?;";
            userId = jdbcTemplate.queryForObject(sql, Integer.class, username);
        } catch (Exception e) {
            throw new NullPointerException("User does not exist with provided username");
        }
        String sql = "SELECT account_id FROM account " +
                "WHERE user_id = ?";
        try {
            Integer accountId = jdbcTemplate.queryForObject(sql, Integer.class, userId);
            return accountId;
        } catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    @Override
    public BigDecimal getBalanceByAccountId(int accountId) {
        BigDecimal balance = BigDecimal.valueOf(0.00);
        String sql = "SELECT balance FROM account WHERE account_id = ?";
        try {
            balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, accountId);
            return balance;
        } catch (EmptyResultDataAccessException e){
            return BigDecimal.valueOf(0.00);
        }
    }


    @Override
    public BigDecimal getBalance(int userId) {
        BigDecimal balance = BigDecimal.valueOf(0.00);
        String sql = "SELECT balance FROM account WHERE user_id = ?";
        balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, userId);

        return balance;
    }

    @Override
    public String updateBalance(String username, TransferDto transferDto) {
        Integer accountTo = getAccountIdFromUsername(transferDto.getUsernameTo());
        Integer accountFrom = getAccountIdFromUsername(username);
        if (accountTo == null){
            return "Error-- User doesn't exist";
        }

        if (!transferDto.getUsernameTo().equals(username) && transferDto.getAmount().compareTo(BigDecimal.valueOf(0.00)) == 1){
            if (transferDAO.subtractFrom(accountFrom, transferDto.getAmount()).equals("Error- There is not enough money in your account to complete transaction. ")){
                return "Error- There is not enough money in your account to complete transaction. ";
            } else {
                transferDAO.addTo(accountTo, transferDto.getAmount());
                String transferStatus = "Approved";
                String transferType = "Send";

                //may change to just be "send" and user needs to read accountTo and accountFrom?
                if (username.equals(transferDto.getUsernameTo())){
                    transferType = "Receive";
                } else {
                    transferType = "Send";
                }
                Transfer transfer = new Transfer(1, accountTo, accountFrom, transferDto.getAmount(), "Approved", transferType);
                transferDAO.createTransfer(transfer);
                return "Transaction Complete.";
            }
        } else {
            return "Error";
        }

    }
}
