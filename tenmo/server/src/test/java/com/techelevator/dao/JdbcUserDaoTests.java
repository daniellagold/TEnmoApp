package com.techelevator.dao;


import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.Username;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class JdbcUserDaoTests extends BaseDaoTests{

    private JdbcUserDao sut;
    private Username Sue = new Username("Sue");


    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcUserDao(jdbcTemplate);


        sut.create("Sue", "Sue");


    }

    @Test
    public void createNewUser() {
        boolean userCreated = sut.create("TEST_USER","test_password");
        Assert.assertTrue(userCreated);
        User user = sut.findByUsername("TEST_USER");
        Assert.assertEquals("TEST_USER", user.getUsername());
    }
    @Test
    public void getAllUsernamesLoggedInReturns2() {
        List<Username> bobsList = sut.getAllUsernamesLoggedIn("bob");
        Assert.assertEquals(2, bobsList.size());
        assertUsersMatch(Sue, bobsList.get(1));

    }
    @Test
    public void findIdByUsernameReturn1001(){
        int actual = sut.findIdByUsername("bob");
        int expected = 1001;
        Assert.assertEquals(expected, actual);

    }
    @Test
    public void findIdByUsernameReturnException(){
        Integer expected = -1;
        Integer actual = sut.findIdByUsername("daniella");
        Assert.assertEquals(expected, actual);

    }


    private void assertUsersMatch(Username expected, Username actual){
        Assert.assertEquals(expected.getUsername(), actual.getUsername());

    }


}
