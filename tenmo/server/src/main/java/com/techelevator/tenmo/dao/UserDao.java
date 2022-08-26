package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.Username;

import java.security.Principal;
import java.util.List;

public interface UserDao {

    List<User> findAll();

    User findByUsername(String username);

    Integer findIdByUsername(String username);

    List<Username> getAllUsernamesLoggedIn(String usernameLoggedIn) throws Exception;

    boolean create(String username, String password);
}
