package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Balance;
import com.techelevator.tenmo.model.Transfer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

public class JdbcAccountDaoTest extends BaseDaoTests{
    protected static final Account ACCOUNT_1 = new Account(new Balance(new BigDecimal("1000")), 1001,2001);
    protected static final Account ACCOUNT_2 = new Account(new Balance(new BigDecimal("1000")), 1002,2002);
    private static final Account ACCOUNT_3 = new Account(new Balance(new BigDecimal("1000")), 1003,2003);

    private JdbcAccountDao sut;
    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcAccountDao(jdbcTemplate);
    }

    @Test
    public void test_get_account_balance_by_account_id(){
        Account actualAccount = sut.getAccountById(ACCOUNT_1.getAccountId());

        Assert.assertEquals(ACCOUNT_1.getBalance().getBalance().setScale(2),actualAccount.getBalance().getBalance());
    }
}
