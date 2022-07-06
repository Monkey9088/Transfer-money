package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Balance;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
@Component
public class JdbcAccountDao implements AccountDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Balance getBalance(String userName) {
        Balance balance = new Balance();
        String sql = "SELECT balance FROM tenmo_account JOIN tenmo_user ON tenmo_user.user_id = tenmo_account.user_id WHERE username = ?; ";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql,userName);
        if(result.next()){
            String balanceString = result.getString("balance");
            balance.setBalance(new BigDecimal(balanceString));
        }
        return balance;

    }

    @Override
    public Account getAccountById(int accountId) {
        Account account = new Account();
        String sql = "SELECT * FROM tenmo_account WHERE account_id = ?; ";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql,accountId);
        if(result.next()){
            account = mapRowToAccount(result);
        }
        return account;
    }

    @Override
    public Account getAccountByUserId(int userId) {
        Account account = new Account();
        String sql = "SELECT account_id, user_id, balance FROM tenmo_account WHERE user_id = ?; ";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql,userId);
        if(result.next()){
            account = mapRowToAccount(result);
        }
        return account;

    }


    private Account mapRowToAccount(SqlRowSet rs) {
        Account account = new Account();


        account.setAccountId(rs.getInt("account_id"));
        account.setUserId(rs.getInt("user_id"));
        Balance balance = new Balance();
        String balanceString = rs.getString("balance");
        balance.setBalance(new BigDecimal(balanceString));
        account.setBalance(balance);
        return account;
    }
}
