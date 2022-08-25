package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exceptions.UserDoesNotExist;
import com.techelevator.tenmo.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao{
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private JdbcUserDao jdbcUserDao;


    public JdbcAccountDao(JdbcTemplate jdbcTemplate, JdbcUserDao jdbcUserDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcUserDao = jdbcUserDao;
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
    public int getAccountIdFromUsername(String username) throws UsernameNotFoundException {
        Integer userId = null;
        try {
            userId = jdbcUserDao.findIdByUsername(username);
        } catch (Exception e) {
            throw new UsernameNotFoundException("User does not exist with provided username");
        }
        System.out.println(userId);
        String sql = "SELECT account_id FROM account " +
                "WHERE user_id = ?";
        Integer accountId = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        if (accountId != null) {
            return accountId;
        } else {
            throw new UsernameNotFoundException("There is no user created for that username");
        }
    }

    @Override
    public BigDecimal getBalanceByAccountId(int accountId) {
        BigDecimal balance = BigDecimal.valueOf(0.00);
        String sql = "SELECT balance FROM account WHERE account_id = ?";
        balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, accountId);
        return balance;
    }


    @Override
    public BigDecimal getBalance(int userId) {
        BigDecimal balance = BigDecimal.valueOf(0.00);
        String sql = "SELECT balance FROM account WHERE user_id = ?";
        balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, userId);

        return balance;
    }

    @Override
    public void updateBalance(int userId, BigDecimal newBalance) {

    }
}
