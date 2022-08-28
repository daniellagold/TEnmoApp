package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcTransferDAO;
import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

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
    private AccountDao accountDao;
    @Autowired
    private TransferDAO transferDAO;


    public TenmoController(UserDao userDao, AccountDao accountDao, TransferDAO transferDAO) {
        this.userDao = userDao;
        this.accountDao = accountDao;
        this.transferDAO = transferDAO;
    }



    @RequestMapping(path = "/balance", method = RequestMethod.GET)
    public BigDecimal getBalanceByUserId(@Valid Principal principal){
        int userId = userDao.findIdByUsername(principal.getName());
        BigDecimal balance = accountDao.getBalance(userId);
        return balance;
    }

    @RequestMapping(path = "/send", method = RequestMethod.GET)
    public List<Username> getUsernamesToSend(@Valid Principal principal) throws Exception {
        return userDao.getAllUsernamesLoggedIn(principal.getName());
    }


    @RequestMapping(path = "/send/transfer", method = RequestMethod.POST)
    public String sendMoney(@Valid Principal principal, @RequestBody TransferDto transferDto) throws UsernameNotFoundException {
        String username = principal.getName();
        return accountDao.updateBalance(username, transferDto);
    }

    @RequestMapping(path = "/transfers", method = RequestMethod.GET)
    public List<Transfer> getTransfersByUser(@Valid Principal principal) throws UsernameNotFoundException {
        int accountId = accountDao.getAccountIdFromUsername(principal.getName());
        List<Transfer> transferList = transferDAO.getTransfersForUser(accountId);
        return transferList;
    }

    @RequestMapping(path = "/transferDetails", method = RequestMethod.GET)
    public Transfer getTransferByTransferId(@Valid Principal principal, @RequestBody TransferIdDTO transferIdDTO) throws UsernameNotFoundException {
        int accountId = accountDao.getAccountIdFromUsername(principal.getName());
        Transfer transfer = transferDAO.getTransferByTransferId(transferIdDTO.getTransferId(), accountId);
        return transfer;
    }

    @RequestMapping(path = "/request", method = RequestMethod.GET)
    public List<Username> getUsernamesToRequest(@Valid Principal principal) throws Exception {
        return userDao.getAllUsernamesLoggedIn(principal.getName());
    }

    @RequestMapping(path = "/request/transfer", method = RequestMethod.POST)
    public String requestMoney(@Valid Principal principal, @RequestBody RequestDto requestDto) throws UsernameNotFoundException {
        String username = principal.getName();
        return accountDao.RequestMoney(principal.getName(), requestDto);
    }

    @RequestMapping(path = "/getRequests", method = RequestMethod.GET)
    public List<Transfer> getRequests(@Valid Principal principal, @RequestParam String transferStatus) {
        int accountId = accountDao.getAccountIdFromUsername(principal.getName());
        List<Transfer> requestList = transferDAO.getTransferByStatus(transferStatus, accountId);
        return requestList;
    }

    @RequestMapping(path = "/approveRequest", method = RequestMethod.PUT)
    public String approveRequest(@Valid Principal principal, @RequestParam int transferId){
        accountDao.approveRequest(transferId);
        int accountId = accountDao.getAccountIdFromUsername(principal.getName());
        String message = accountDao.updateBalanceAfterRequest(transferId, accountId);
        
        return message;

    }

}
