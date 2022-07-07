package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Balance;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.PortResolverImpl;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.dsig.TransformService;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/tenmo")
public class TenmoController {
    private UserDao userDao;
    private AccountDao accountDao;
    private TransferDao transferDao;

    public TenmoController(UserDao userDao, AccountDao accountDao,TransferDao transferDao) {
        this.userDao = userDao;
        this.accountDao = accountDao;
        this.transferDao = transferDao;
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

    @GetMapping("/users")
    public List<User> getAllUsers(){
        return userDao.findAll();
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable int id){
        return userDao.getUserById(id);
    }

    @PostMapping("/transfer")
    @ResponseStatus(HttpStatus.CREATED)
    public void transfer(@RequestBody Transfer transfer){
        transferDao.transfer(transfer);
    }

    @PutMapping("/transfer/approve")
    public void transferApprove(@RequestBody Transfer transfer){
        transferDao.transferApprove(transfer);
    }

    @PutMapping("/transfer/reject")
    public void transferReject(@RequestBody Transfer transfer){
        transferDao.transferReject(transfer);
    }

    @GetMapping("/transfer/user/{id}")
    public List<Transfer> getAllTransferById(@PathVariable int id){
        return transferDao.listAllByAccountId(id);
    }

    @GetMapping("/transfer/{id}")
    public Transfer getByTransferId(@PathVariable int id){
        return transferDao.getByTransferId(id);
    }




}
