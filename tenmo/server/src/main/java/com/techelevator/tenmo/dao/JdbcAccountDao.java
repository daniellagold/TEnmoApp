package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.math.BigDecimal;

public class JdbcAccountDao implements AccountDao{
    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
    public Account getAccount(int userId) {
        return null;
    }

    @Override
    public BigDecimal getBalance(int accountId) {
        return null;
    }
}
