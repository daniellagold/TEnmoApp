package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcTransferDAO;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.exceptions.UserDoesNotExist;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDto;
import com.techelevator.tenmo.model.TransferIdDTO;
import com.techelevator.tenmo.model.Username;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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


    public TenmoController(UserDao userDao, JdbcAccountDao jdbcAccountDao, JdbcTransferDAO jdbcTransferDAO) {
        this.userDao = userDao;
        this.jdbcAccountDao = jdbcAccountDao;
        this.jdbcTransferDAO = jdbcTransferDAO;
    }



    @RequestMapping(path = "/balance", method = RequestMethod.GET)
    public BigDecimal getBalanceByUserId(@Valid Principal principal){
        int userId = userDao.findIdByUsername(principal.getName());
        BigDecimal balance = jdbcAccountDao.getBalance(userId);
        return balance;
    }

    @RequestMapping(path = "/send", method = RequestMethod.GET)
    public List<Username> getUsernamesToSend(@Valid Principal principal) throws Exception {
        return userDao.getAllUsernamesLoggedIn(principal.getName());
    }


    @RequestMapping(path = "/send/transfer", method = RequestMethod.POST)
    public String sendMoney(@Valid Principal principal, @RequestBody TransferDto transferDto) throws UsernameNotFoundException {
        String username = principal.getName();
        return jdbcAccountDao.updateBalance(username, transferDto);
    }

    @RequestMapping(path = "/transfers", method = RequestMethod.GET)
    public List<Transfer> getTransfersByUser(@Valid Principal principal) throws UsernameNotFoundException {
        int accountId = jdbcAccountDao.getAccountIdFromUsername(principal.getName());
        List<Transfer> transferList = jdbcTransferDAO.getTransfersForUser(accountId);
        return transferList;
    }

    @RequestMapping(path = "/transferDetails", method = RequestMethod.GET)
    public Transfer getTransferByTransferId(@Valid Principal principal, @RequestBody TransferIdDTO transferIdDTO) throws UsernameNotFoundException {
        int accountId = jdbcAccountDao.getAccountIdFromUsername(principal.getName());
        Transfer transfer = jdbcTransferDAO.getTransferByTransferId(transferIdDTO.getTransferId(), accountId);
        return transfer;
    }

}
