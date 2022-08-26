package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.Username;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcUserDao implements UserDao {

    private static BigDecimal startingBalance = new BigDecimal(1000.00);
    private JdbcTemplate jdbcTemplate;

    public JdbcUserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Integer findIdByUsername(String username) {
        String sql = "SELECT user_id FROM tenmo_user WHERE username ILIKE ?;";
        try {
            Integer id = jdbcTemplate.queryForObject(sql, Integer.class, username);
            return id;
        } catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT user_id, username, password_hash FROM tenmo_user;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while(results.next()) {
            User user = mapRowToUser(results);
            users.add(user);
        }
        return users;
    }


    @Override
    public User findByUsername(String username) throws UsernameNotFoundException {
        String sql = "SELECT user_id, username, password_hash FROM tenmo_user WHERE username ILIKE ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, username);
        if (rowSet.next()){
            return mapRowToUser(rowSet);
        }
        throw new UsernameNotFoundException("User " + username + " was not found.");
    }

    @Override
    public List<Username> getAllUsernamesLoggedIn(String usernameLoggedIn) {
        List<Username> usernameList = new ArrayList<>();
        String sql = "SELECT user_id, username FROM tenmo_user WHERE username != ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, usernameLoggedIn);
        while (rowSet.next()){
            Username username = mapRowToUsername(rowSet);
            usernameList.add(username);
        } if (usernameList.size()<1){
            throw new RuntimeException("No other users");
        }
        return usernameList;
    }

    @Override
    public boolean create(String username, String password) {

        // create user
        String sql = "INSERT INTO tenmo_user (username, password_hash) VALUES (?, ?) RETURNING user_id";
        String password_hash = new BCryptPasswordEncoder().encode(password);
        Integer newUserId;
        try {
            newUserId = jdbcTemplate.queryForObject(sql, Integer.class, username, password_hash);
        } catch (DataAccessException e) {
            return false;
        }

        // TODO: Create the account record with initial balance
        String sqlStatement = "INSERT INTO account (user_id, balance) VALUES (?, ?) RETURNING account_id;";
        Integer newAccountId;

        try {
            newAccountId = jdbcTemplate.queryForObject(sqlStatement, Integer.class, newUserId, startingBalance);
        } catch (Exception e){
            return false;
        }
        return true;
    }

    private User mapRowToUser(SqlRowSet rs) {
        User user = new User();
        user.setId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password_hash"));
        user.setActivated(true);
        user.setAuthorities("USER");
        return user;
    }

    private Username mapRowToUsername(SqlRowSet rs){
        Username username = new Username();
        username.setId(rs.getInt("user_id"));
        username.setUsername(rs.getString("username"));
        return username;
    }

}
