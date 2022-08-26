package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcTransferDAO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

public class JdbcTransferTests extends BaseDaoTests{

    private JdbcTransferDAO sut;

    @Before
    public void setup(){
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcTransferDAO(jdbcTemplate);
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


}
