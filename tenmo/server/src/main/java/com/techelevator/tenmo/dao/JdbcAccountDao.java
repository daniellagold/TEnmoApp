package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.data.relational.core.sql.In;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
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
    public int getAccountIdFromUsername(String username){
        String sql = "SELECT account_id FROM account " +
                "JOIN tenmo_user ON account.user_id = tenmo_user.user_id " +
                "WHERE username = ?";
        Integer id = jdbcTemplate.queryForObject(sql, Integer.class, username);
//        if (id != null) {
//            return id;
//        } else {
//            return -1;
//        }
        return id;
    }


    @Override
    public BigDecimal getBalance(int userId) {
        BigDecimal balance = new BigDecimal(0.00);
        String sql = "SELECT balance FROM account WHERE user_id = ?";
        balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, userId);

        return balance;
    }

    @Override
    public void updateBalance(int userId, BigDecimal newBalance) {

    }
}
