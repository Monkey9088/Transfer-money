package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.*;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class ApplicationService {

    private final String API_BASE_URL = "http://localhost:8080/tenmo";
    private final RestTemplate restTemplate = new RestTemplate();

    private String authToken = null;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public Balance getBalance(AuthenticatedUser authenticatedUser){
        Balance balance = null;
        try {
            ResponseEntity<Balance> response =
                    restTemplate.exchange(API_BASE_URL + "/balance", HttpMethod.GET, makeAuthEntity(authenticatedUser), Balance.class);
            balance = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return balance;
    }

    public Account getAccountById(AuthenticatedUser authenticatedUser, int accountId){
        Account account= null;
        try {
            ResponseEntity<Account> response =
                    restTemplate.exchange(API_BASE_URL + "/account/" + accountId, HttpMethod.GET, makeAuthEntity(authenticatedUser), Account.class);
            account = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return account;
    }

    public Account getAccountByUserId(AuthenticatedUser authenticatedUser, int userId){
        Account account= null;
        try {
            ResponseEntity<Account> response =
                    restTemplate.exchange(API_BASE_URL + "/account/user/" + userId, HttpMethod.GET, makeAuthEntity(authenticatedUser), Account.class);
            account = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return account;
    }

    public User[] viewAllUsers(AuthenticatedUser authenticatedUser){
        User[] users = null;
        try {
            ResponseEntity<User[]> response =
                    restTemplate.exchange(API_BASE_URL + "/users", HttpMethod.GET, makeAuthEntity(authenticatedUser), User[].class);
            users = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return users;
    }

    public User getUser(AuthenticatedUser authenticatedUser,int userId){
        User user = null;
        try {
            ResponseEntity<User> response =
                    restTemplate.exchange(API_BASE_URL + "/users/" + userId, HttpMethod.GET, makeAuthEntity(authenticatedUser), User.class);
            user = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return user;
    }

    public Transfer[] getTransferHistory(AuthenticatedUser authenticatedUser, int accountId){
        Transfer[] transfers = null;
        try {
            ResponseEntity<Transfer[]> response =
                    restTemplate.exchange(API_BASE_URL + "/transfer/user/" + accountId, HttpMethod.GET, makeAuthEntity(authenticatedUser), Transfer[].class);
            transfers = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfers;
    }

    public Transfer getTransfer(AuthenticatedUser authenticatedUser, int transferId){
        Transfer transfer = null;
        try {
            ResponseEntity<Transfer> response =
                    restTemplate.exchange(API_BASE_URL + "/transfer/" + transferId, HttpMethod.GET, makeAuthEntity(authenticatedUser), Transfer.class);
            transfer = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfer;
    }

    public void approveTransfer(AuthenticatedUser authenticatedUser,Transfer transfer){
        try {
            restTemplate.exchange(API_BASE_URL + "/transfer/approve", HttpMethod.PUT, makeTransferEntity(authenticatedUser, transfer), Transfer.class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
    }

    public void rejectTransfer(AuthenticatedUser authenticatedUser,Transfer transfer){
        try {
            restTemplate.exchange(API_BASE_URL + "/transfer/reject", HttpMethod.PUT, makeTransferEntity(authenticatedUser, transfer), Transfer.class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
    }

    public void sendTeMoney(AuthenticatedUser authenticatedUser, Transfer transfer){
        try {
            restTemplate.exchange(API_BASE_URL + "/transfer", HttpMethod.POST, makeTransferEntity(authenticatedUser, transfer), Transfer.class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
    }




    private HttpEntity<Transfer> makeTransferEntity(AuthenticatedUser authenticatedUser, Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        //headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authenticatedUser.getToken());
        return new HttpEntity<>(transfer, headers);
    }

    private HttpEntity<Void> makeAuthEntity(AuthenticatedUser authenticatedUser) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        return new HttpEntity<>(headers);
    }
}
