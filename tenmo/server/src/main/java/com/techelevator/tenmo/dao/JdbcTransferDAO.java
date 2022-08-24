package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
@Component
public class JdbcTransferDAO implements TransferDAO{
    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void subtractFrom(int accountId, BigDecimal amount) {
        String sql = "UPDATE account SET balance = ((SELECT balance FROM account WHERE account_id = ?) - ?) WHERE account_id = ?";
        jdbcTemplate.update(sql, BigDecimal.class, accountId, amount, accountId);
    }

    @Override
    public void addTo(int userId, BigDecimal amount) {

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
