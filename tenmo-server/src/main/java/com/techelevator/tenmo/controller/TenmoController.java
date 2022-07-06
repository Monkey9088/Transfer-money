package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Balance;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.PortResolverImpl;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/tenmo")
public class TenmoController {
    private UserDao userDao;
    private AccountDao accountDao;

    public TenmoController(UserDao userDao, AccountDao accountDao) {
        this.userDao = userDao;
        this.accountDao = accountDao;
    }
 @RequestMapping(path = "/balance", method = RequestMethod.GET)
    public Balance getBalance(Principal principal){
        return accountDao.getBalance(principal.getName());
    }
 @RequestMapping(path = "/account/{id}", method = RequestMethod.GET)
    public Account getAccountByAccountId(@PathVariable int id){
        return accountDao.getAccountById(id);
    }
  @RequestMapping(path = "/account/user/{id}", method = RequestMethod.GET)
    public Account getAccountByUserId(@PathVariable int id ){
        return accountDao.getAccountByUserId(id);
    }
}
