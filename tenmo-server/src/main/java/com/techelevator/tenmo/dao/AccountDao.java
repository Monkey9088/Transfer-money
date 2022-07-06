package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Balance;

public interface AccountDao {
    Balance getBalance(String userName);
    Account getAccountById(int accountId);
    Account getAccountByUserId(int userId);
}
