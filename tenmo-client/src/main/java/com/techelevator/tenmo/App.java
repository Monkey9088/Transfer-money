package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.ApplicationService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import org.springframework.beans.NullValueInNestedPathException;

import java.math.BigDecimal;
import java.sql.SQLOutput;
import java.util.Arrays;

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
           if(transfer.getTransferAccountFrom()==accountId && transfer.getTransferStatusId() == 2){
               String userName = applicationService.getUser(currentUser,
                       applicationService.getAccountById(currentUser,transfer.getTransferAccountTo()).getUserId()).getUsername();
               System.out.println("Transfer ID" + " " + transfer.getTransferId() + " "+ "receiver" + " "+ userName + " "+ "$amount" + " " + transfer.getAmount());
           }
           if(transfer.getTransferAccountTo()==accountId && transfer.getTransferStatusId() == 2){
               String userName = applicationService.getUser(currentUser,
                       applicationService.getAccountById(currentUser,transfer.getTransferAccountFrom()).getUserId()).getUsername();
               System.out.println("Transfer ID" + " " + transfer.getTransferId() + " "+ "from" + " "+ userName + " "+ "$amount" + " " + transfer.getAmount());
           };
       }
        //System.out.println("History will be listed Here!");

    }

	private void viewPendingRequests() {
        Account currentAccount = applicationService.getAccountByUserId(currentUser, Math.toIntExact(currentUser.getUser().getId()));
        int accountId = currentAccount.getAccountId();
        Transfer[] transfers = applicationService.getTransferHistory(currentUser, accountId);
        for(Transfer transfer : transfers){
            if(transfer.getTransferStatusId()==1 &&
                    transfer.getTransferAccountFrom()== currentAccount.getAccountId()){
                String senderName = applicationService.getUser(currentUser,
                        applicationService.getAccountById(currentUser,transfer.getTransferAccountFrom()).getUserId()).getUsername();
                String receiverName = applicationService.getUser(currentUser,
                        applicationService.getAccountById(currentUser,transfer.getTransferAccountTo()).getUserId()).getUsername();
                System.out.println("ID" + " " + transfer.getTransferId() + " - "+ "from" + " "+ transfer.getTransferAccountFrom() + " - " +
                        senderName + " To: " + transfer.getTransferAccountTo()+" - " + receiverName + " "+ "$ Amount:" + " " + transfer.getAmount());
            }
        }

        int transferId = consoleService.promptForInt("Please enter the ID you would like to Approve: \n");
        Transfer selectedTransfer = applicationService.getTransfer(currentUser,transferId);
            int menuSelection = -1;
            while (menuSelection != 0 && transferId != 0 && transferId == selectedTransfer.getTransferId()) {
                consoleService.printStatusMenu();
                menuSelection = consoleService.promptForMenuSelection("Please Select Action: ");
                if (menuSelection == 1) {
                    selectedTransfer.setTransferStatusId(2);
                    applicationService.approveTransfer(currentUser,selectedTransfer);
                    System.out.println("Pending Transfer Successfully Approved!");
                    System.out.println("Your Current Balance is: " +applicationService.getBalance(currentUser));
                    consoleService.pause();
                    mainMenu();
                } else if (menuSelection == 2) {
                    applicationService.rejectTransfer(currentUser,selectedTransfer);
                    System.out.println("Pending Transfer Successfully Rejected!");
                    consoleService.pause();
                    mainMenu();
                } else if (menuSelection == 0) {
                    continue;
                } else {
                    System.out.println("Invalid Selection");
                    consoleService.pause();
                    mainMenu();
                }
            }
	}

	private void sendBucks() {
		// TODO Auto-generated method stub
        //List the users and select among them
        User[] users = applicationService.viewAllUsers(currentUser);
        System.out.println("Select Receiver");
        for(User user : users){
            if(user.getUsername().equals(currentUser.getUser().getUsername()))
                continue;
            System.out.println(user.getId() + "- " + user.getUsername());
        }
        int sendToUserId = consoleService.promptForInt("Please enter the User Id you would like to send Bucks: \n");
        Account sendToAccount = applicationService.getAccountByUserId(currentUser, sendToUserId);
        int sendToAccountId = sendToAccount.getAccountId();



        Account currentAccount = applicationService.getAccountByUserId(currentUser, Math.toIntExact(currentUser.getUser().getId()));
        int accountId = currentAccount.getAccountId();

        //Manual UserId input
        /*int sendToUserId = consoleService.promptForInt("Please enter the User Id you would like to send Bucks: ");
        Account sendToAccount = applicationService.getAccountByUserId(currentUser, sendToUserId);
        int sendToAccountId = sendToAccount.getAccountId();*/

        if(sendToUserId == currentAccount.getUserId()) {
            System.out.println("Sending money to yourself is not permitted!");
            consoleService.pause();
            mainMenu();
        }/*else if(Arrays.stream(users).anyMatch(n->n.getId() != sendToUserId)){
            System.out.println("User ID is invalid. Please Try Again!");
            consoleService.pause();
            mainMenu();
        }*/else{
            BigDecimal transferAmount = consoleService.promptForBigDecimal("Please enter the Amount: \n");
            if(transferAmount.compareTo(new BigDecimal("0")) <= 0){
                System.out.println("The Amount must be greater than 0!");
                consoleService.pause();
                mainMenu();
            }
            if(transferAmount.compareTo(applicationService.getBalance(currentUser).getBalance()) >= 1){
                System.out.println("The Amount must be less than your Current Balance!");
                consoleService.pause();
                mainMenu();
            }
            Transfer newTransfer = new Transfer();
            newTransfer.setTransferTypeId(2);
            newTransfer.setTransferStatusId(2);
            newTransfer.setAmount(transferAmount);
            newTransfer.setTransferAccountTo(sendToAccountId);
            newTransfer.setTransferAccountFrom(accountId);

            applicationService.sendTeMoney(currentUser, newTransfer);
            System.out.println(transferAmount + " TE Bucks Succesfully Sent to " + applicationService.getUser(currentUser, applicationService.getAccountById(currentUser,sendToAccountId).getUserId()).getUsername());
            System.out.println("Your Current Balance is " + applicationService.getBalance(currentUser));
        }

        //System.out.println("TE Bucks successfully Sent!");



	}

	private void requestBucks() {
        //List Users
        User[] users = applicationService.viewAllUsers(currentUser);
        System.out.println("Select User to Request Bucks");
        for(User user : users){
            if(user.getUsername().equals(currentUser.getUser().getUsername()))
                continue;
            System.out.println(user.getId() + "- " + user.getUsername());
        }
        int requestFromUserId = consoleService.promptForInt("Please enter the User Id you would like to request Bucks: \n");
        Account requestFromAccount = applicationService.getAccountByUserId(currentUser, requestFromUserId);
        int requestFromAccountId = requestFromAccount.getAccountId();

        Account currentAccount = applicationService.getAccountByUserId(currentUser, Math.toIntExact(currentUser.getUser().getId()));
        int accountId = currentAccount.getAccountId();

        /*int sendToUserId = consoleService.promptForInt("Please enter the User Id you would like to request Bucks: ");
        Account sendToAccount = applicationService.getAccountByUserId(currentUser, sendToUserId);
        int sendToAccountId = sendToAccount.getAccountId();*/

        if(requestFromUserId == currentAccount.getUserId()){
            System.out.println("Requesting money from yourself is not permitted");
            consoleService.pause();
        }else {
            BigDecimal transferAmount = consoleService.promptForBigDecimal("Please enter the Amount: ");
            if (transferAmount.compareTo(new BigDecimal("0")) <= 0) {
                System.out.println("The Amount must be greater than 0!");
                consoleService.pause();
            }


            Transfer newTransfer = new Transfer();
            newTransfer.setTransferTypeId(1);
            newTransfer.setTransferStatusId(1);
            newTransfer.setAmount(transferAmount);
            newTransfer.setTransferAccountTo(accountId);
            newTransfer.setTransferAccountFrom(requestFromAccountId);

            applicationService.sendTeMoney(currentUser, newTransfer);
            System.out.println("$ " + transferAmount + " Your request has been sent to " +
                    applicationService.getUser(currentUser, applicationService.getAccountById(currentUser,requestFromAccountId).getUserId()).getUsername());



        }
		
	}

}
