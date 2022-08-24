package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcTransferDAO;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.Username;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TenmoController {
    @Autowired
    private UserDao userDao;
    @Autowired
    private JdbcAccountDao jdbcAccountDao;
    @Autowired
    private JdbcTransferDAO jdbcTransferDAO;


    public TenmoController(UserDao userDao) {
        this.userDao = userDao;
    }



    @RequestMapping(path = "/balance", method = RequestMethod.GET)
    public BigDecimal getBalanceByUserId(@Valid Principal principal){
        int userId = userDao.findIdByUsername(principal.getName());
        BigDecimal balance = jdbcAccountDao.getBalance(userId);
        return balance;
    }

    @RequestMapping(path = "/send", method = RequestMethod.GET)
    public List<Username> getUsernamesToSend(@Valid Principal principal){
        return userDao.getAllUsernamesLoggedIn(principal.getName());
    }

    //THIS IS NOT WORKING...not returning id in the getAccountIdFromUsername...but the sql statement works fine.
    @RequestMapping(path = "/send/transfer", method = RequestMethod.POST)
    public boolean sendMoney(@Valid Principal principal, @RequestBody String username, BigDecimal amount){
        int accountTo = jdbcAccountDao.getAccountIdFromUsername(username);
        int accountFrom = jdbcAccountDao.getAccountIdFromUsername(principal.getName());
        jdbcTransferDAO.subtractFrom(accountFrom, amount);
        //put add method here as well
        Transfer transfer = new Transfer(1, accountTo, accountFrom, amount, "Approved", "Send");
        return jdbcTransferDAO.createTransfer(transfer);

    }

}
