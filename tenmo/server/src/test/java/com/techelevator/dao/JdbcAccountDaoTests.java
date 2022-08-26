package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcTransferDAO;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.TransferDto;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.Username;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;

import java.math.BigDecimal;
import java.security.Principal;

public class JdbcAccountDaoTests extends BaseDaoTests {

    private JdbcAccountDao sut;
    private TransferDto transferDto = new TransferDto();
    private JdbcTransferDAO jdbcTransferDAO;



    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTransferDAO = new JdbcTransferDAO(jdbcTemplate);
        sut = new JdbcAccountDao(jdbcTemplate, jdbcTransferDAO);
        jdbcTransferDAO.setAccountDao(sut);


    }

    @Test
    public void createAccountReturnsTrue(){
        boolean accountCreated = sut.createAccount(1002, BigDecimal.valueOf(1000.00));
        Assert.assertTrue(accountCreated);
    }

    @Test
    public void createAccountReturnsFalse(){
        boolean accountCreated = sut.createAccount(1003, BigDecimal.valueOf(1000.00));
        Assert.assertFalse(accountCreated);
    }

    @Test
    public void getAccountIdFromUsernameReturnsID(){
        Integer expected = 2002;
        Integer actual = sut.getAccountIdFromUsername("user");
        Assert.assertEquals(expected, actual);

    }

    @Test (expected = NullPointerException.class)
    public void getAccountIdFromUsernameReturnsNull(){
        Integer actual = sut.getAccountIdFromUsername("aldkfjd");
    }

    @Test
    public void getBalanceByAccountIdReturns1000(){
        BigDecimal expected = BigDecimal.valueOf(1000.00);
        BigDecimal actual = sut.getBalanceByAccountId(2001);

        Assert.assertTrue(expected.compareTo(actual) == 0);
    }

    @Test
    public void getBalanceByAccountIdReturns0(){
        BigDecimal expected = BigDecimal.valueOf(0.00);
        BigDecimal actual = sut.getBalanceByAccountId(2005);

        Assert.assertTrue(expected.compareTo(actual) == 0);
    }

    @Test
    public void updateBalanceReturnsTransactionComplete(){
        transferDto.setAmount(BigDecimal.valueOf(100.00));
        transferDto.setUsernameFrom("bob");
        transferDto.setUsernameTo("user");
        String expected = "Transaction Complete.";
        String actual = sut.updateBalance("bob", transferDto);

        Assert.assertEquals(expected, actual);
   }

   @Test
    public void updateBalanceReturnsNotEnoughMoney(){
        transferDto.setAmount(BigDecimal.valueOf(2000.00));
        transferDto.setUsernameFrom("bob");
        transferDto.setUsernameTo("user");
        String expected =  "Error- There is not enough money in your account to complete transaction. ";
        String actual = sut.updateBalance("bob", transferDto);
        Assert.assertEquals(expected, actual);
   }






}
