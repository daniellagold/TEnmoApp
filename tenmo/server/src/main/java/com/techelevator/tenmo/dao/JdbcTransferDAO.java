package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDAO implements TransferDAO{
    private JdbcTemplate jdbcTemplate;
    private JdbcAccountDao jdbcAccountDao;


    public JdbcTransferDAO(JdbcTemplate jdbcTemplate, JdbcAccountDao jdbcAccountDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcAccountDao = jdbcAccountDao;
    }

    @Override
    public String subtractFrom(int accountId, BigDecimal amount) {

        if (amount.compareTo(jdbcAccountDao.getBalanceByAccountId(accountId))== 0 || amount.compareTo(jdbcAccountDao.getBalanceByAccountId(accountId))== -1 ){
            String sql = "UPDATE account SET balance = (balance - ?) WHERE account_id = ?";
            jdbcTemplate.update(sql, amount, accountId);

            return "Transaction Complete.";
        } else {
            return "Error- There is not enough money in your account to complete transaction. ";
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

    @Override
    public List<Transfer> getTransfersForUser(int accountId) {
        List<Transfer> transferList = new ArrayList<>();
        String sql = "SELECT * FROM transfer WHERE account_to = ? OR account_from = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, accountId, accountId);
        while (rowSet.next()){
            Transfer transfer = mapRowToTransfer(rowSet);
            transferList.add(transfer);
        }
        return transferList;
    }

    @Override
    public Transfer getTransferByTransferId(int transferId, int accountId) {
        Transfer transfer = new Transfer();
        String sql = "SELECT * FROM transfer WHERE transfer_id = ? AND (account_to = ? OR account_from = ?)";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, transferId, accountId, accountId);
        if (rowSet.next()){
            transfer = mapRowToTransfer(rowSet);
        } else {
            throw new RuntimeException("Not authorized");
        }
        return transfer;
    }

    public Transfer mapRowToTransfer(SqlRowSet rowSet){
        Transfer transfer = new Transfer();
        transfer.setTransferId(rowSet.getInt("transfer_id"));
        transfer.setAccountTo(rowSet.getInt("account_to"));
        transfer.setAccountFrom(rowSet.getInt("account_from"));
        transfer.setAmount(rowSet.getBigDecimal("amount"));
        transfer.setTransferStatus(rowSet.getString("transfer_status"));
        transfer.setTransferType(rowSet.getString("transfer_type"));
        return transfer;
    }


}
