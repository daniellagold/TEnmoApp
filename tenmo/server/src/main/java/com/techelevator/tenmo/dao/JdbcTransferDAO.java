package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
@Component
public class JdbcTransferDAO implements TransferDAO{
    private JdbcTemplate jdbcTemplate;
    private JdbcAccountDao jdbcAccountDao;


    public JdbcTransferDAO(JdbcTemplate jdbcTemplate, JdbcAccountDao jdbcAccountDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcAccountDao = jdbcAccountDao;
    }

    @Override
    public boolean subtractFrom(int accountId, BigDecimal amount) {
        boolean success = false;
        if (amount.compareTo(jdbcAccountDao.getBalanceByAccountId(accountId))== 0 || amount.compareTo(jdbcAccountDao.getBalanceByAccountId(accountId))== -1 ){
            String sql = "UPDATE account SET balance = (balance - ?) WHERE account_id = ?";
            jdbcTemplate.update(sql, amount, accountId);
            success = true;
            return success;
        } else {
            return success;
        }

    }

    @Override
    public void addTo(int accountId, BigDecimal amount) {
        String sql = "UPDATE account SET balance = (balance + ?) WHERE account_id = ?";
        jdbcTemplate.update(sql, amount, accountId);

    }

    @Override
    public boolean createTransfer(Transfer transfer) {
        boolean success = false;
        Integer newTransferId;
        String sql = "INSERT INTO transfer (account_to, account_from, amount, transfer_status, transfer_type)" +
                " VALUES (?, ?, ?, ?, ?) RETURNING transfer_id";
        try{
            newTransferId = jdbcTemplate.queryForObject(sql, Integer.class, transfer.getAccountTo(), transfer.getAccountFrom(), transfer.getAmount(), transfer.getTransferStatus(), transfer.getTransferType());
            success = true;
        } catch (Exception e){
            success = false;
        }
        return success;
    }
}
