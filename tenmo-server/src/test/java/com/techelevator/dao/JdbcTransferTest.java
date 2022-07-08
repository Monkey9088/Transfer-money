package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

public class JdbcTransferTest extends BaseDaoTests{

    protected static final Transfer TRANSFER_1 = new Transfer(3001, 2, 2, 2004,2005,BigDecimal.TEN);
    protected static final Transfer TRANSFER_2 = new Transfer(3002, 1, 2, 2005,2006,new BigDecimal("3"));
    private static final Transfer TRANSFER_3 = new Transfer(3003, 2, 2, 2006,2004,BigDecimal.ONE);

    private JdbcTransferDao sut;
    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcTransferDao(jdbcTemplate);
    }

    @Test
    public void test_get_transfer_amount_by_transfer_id(){
        Transfer actualTransfer = sut.getByTransferId(TRANSFER_1.getTransferId());

        Assert.assertEquals(TRANSFER_1.getAmount().setScale(2),actualTransfer.getAmount());
    }
}
