package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.ApplicationService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import org.springframework.beans.NullValueInNestedPathException;

import java.math.BigDecimal;
import java.sql.SQLOutput;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();

    private final ApplicationService applicationService = new ApplicationService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);

    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() {
		// TODO Auto-generated method stub-- DONE
        // DONE
		Balance balance = applicationService.getBalance(currentUser);
        System.out.println(balance.toString());
	}

	private void viewTransferHistory() {
		// TODO Auto-generated method stub
		Account currentAccount = applicationService.getAccountByUserId(currentUser, Math.toIntExact(currentUser.getUser().getId()));
        int accountId = currentAccount.getAccountId();
        Transfer[] transfers = applicationService.getTransferHistory(currentUser, accountId);
        for(Transfer transfer : transfers){
            System.out.println(transfer.toString());
       }
        //System.out.println("History will be listed Here!");

    }

	private void viewPendingRequests() {
		// TODO Auto-generated method stub
		
	}

	private void sendBucks() {
		// TODO Auto-generated method stub
        Account currentAccount = applicationService.getAccountByUserId(currentUser, Math.toIntExact(currentUser.getUser().getId()));
        int accountId = currentAccount.getAccountId();

        int sendToUserId = consoleService.promptForInt("Please enter the User Id you would like to send Bucks.");
        Account sendToAccount = applicationService.getAccountByUserId(currentUser, sendToUserId);
        int sendToAccountId = sendToAccount.getAccountId();

        if(sendToUserId == currentAccount.getUserId()){
            System.out.println("Sending money to yourself is not permitted!");
            consoleService.pause();
        }else{
            BigDecimal transferAmount = consoleService.promptForBigDecimal("Please enter the Amount.");
            if(transferAmount.compareTo(new BigDecimal("0")) <= 0){
                System.out.println("The Amount must be greater than 0!");
                consoleService.pause();
            }
            if(transferAmount.compareTo(applicationService.getBalance(currentUser).getBalance()) >= 1){
                System.out.println("The Amount must be less than your Current Balance!");
                consoleService.pause();
            }
            Transfer newTransfer = new Transfer();
            newTransfer.setTransferTypeId(2);
            newTransfer.setTransferStatusId(2);
            newTransfer.setAmount(transferAmount);
            newTransfer.setTransferAccountTo(sendToAccountId);
            newTransfer.setTransferAccountFrom(accountId);

            applicationService.sendTeMoney(currentUser, newTransfer);
            System.out.println(transferAmount + " TE Bucks Succesfully Sent to " + sendToAccountId);
            System.out.println("Your Current Balance is " + currentAccount.getBalance());
        }

        //System.out.println("TE Bucks successfully Sent!");



	}

	private void requestBucks() {
		// TODO Auto-generated method stub
		
	}

}
