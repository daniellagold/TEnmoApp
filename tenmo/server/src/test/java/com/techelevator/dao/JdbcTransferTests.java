package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcTransferDAO;
import com.techelevator.tenmo.model.Transfer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.List;

public class JdbcTransferTests extends BaseDaoTests{

    private JdbcTransferDAO sut;
    private JdbcAccountDao jdbcAccountDao;
    private Transfer transfer = new Transfer();

    @Before
    public void setup(){
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcAccountDao = new JdbcAccountDao(jdbcTemplate);
        sut = new JdbcTransferDAO(jdbcTemplate, jdbcAccountDao);
        jdbcAccountDao.setTransferDAO(sut);

    }

    @Test
    public void addToReturnsTrue(){
        boolean actual = sut.addTo(2001, BigDecimal.valueOf(100.00));

        Assert.assertTrue(actual);
    }

    @Test
    public void addToReturnsFalse(){
        boolean actual = sut.addTo(2006909, BigDecimal.valueOf(100.00));

        Assert.assertEquals(false, actual);
    }
    @Test
    public void subtractFromReturnsTransactionComplete(){
        String actual = sut.subtractFrom(2001, BigDecimal.valueOf(50.00));
        String expected = "Transaction Complete.";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void subtractFromReturnsError(){
        String actual = sut.subtractFrom(4040, BigDecimal.valueOf(100.00));
        String expected = "Error- There is not enough money in your account to complete transaction. ";
        Assert.assertEquals(expected, actual);
    }
    @Test
    public void createTransferReturnTrue(){
        transfer.setTransferId(3001);
        transfer.setAccountFrom(2001);
        transfer.setAccountTo(2002);
        transfer.setAmount(BigDecimal.valueOf(10.00));
        transfer.setTransferType("Send");
        transfer.setTransferStatus("Approved");

        boolean success = sut.createTransfer(transfer);
        Assert.assertEquals(true, success);
    }
    @Test
    public void createTransferReturnsFalse(){
        transfer.setTransferId(3001);
        transfer.setAccountFrom(0303);
        transfer.setAccountTo(0202);
        transfer.setTransferStatus("Approved");
        transfer.setTransferType("Send");
        boolean success = sut.createTransfer(transfer);
        Assert.assertEquals(false, success);
    }

    @Test
    public void getTransfersForUserReturns2(){
        List<Transfer> list = sut.getTransfersForUser(2002);
        Assert.assertEquals(2, list.size());
    }

    @Test
    public void getTransferByTransferIdReturnsTransfer(){
        Transfer expected = new Transfer(3001, 2002, 2001, BigDecimal.valueOf(10.00), "Approved", "Send");
        Transfer actual = sut.getTransferByTransferId(3001, 2001);
        Assert.assertTrue(expected.getAmount().compareTo(actual.getAmount())==0);
        Assert.assertEquals(expected.getAccountTo(), actual.getAccountTo());

    }




}
