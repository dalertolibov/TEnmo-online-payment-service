package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.UserService;

import java.math.BigDecimal;
import java.util.List;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private AuthenticatedUser currentUser;
    private UserService userService= new UserService(API_BASE_URL);



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
        userService.setCurrentUser(currentUser);
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
 //               requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
           // consoleService.pause();
        }
    }

	private void viewCurrentBalance() {

        //String token=currentUser.getToken();
        BigDecimal balance=userService.getBalance();
        if(balance!=null){
        System.out.println("\nYour current account balance is: $"+balance);
        }
        else
            consoleService.printErrorMessage();
    }

 //


    private void viewTransferHistory() {
        consoleService.promptForListTransfers();
        List<Transfer>approvedTransfers=userService.getApprovedTransfers();
        transferHistoryPrinter(approvedTransfers);
        consoleService.pause();
    }

	private void viewPendingRequests() {
        consoleService.promptForPendingRequests();
        List<Transfer>pendingTransfers=userService.getPendingTransfers();
        transferHistoryPrinter(pendingTransfers);
        consoleService.pause();

    }





	private void sendBucks() {
        consoleService.promptForUsersHeader();
        User[]allUser= userService.getAllUsers();
        if(allUser!=null){
            for(User user:allUser){
                String formattedString=String.format("%-10d %s",user.getId(),user.getUsername().toUpperCase());
                System.out.println(formattedString);
            }
        }else consoleService.printErrorMessage();
        long userId= (consoleService.promptForInt("Enter ID of user you are sending to (0 to cancel):"));
        if(userId==0){
            return;
        }else if(userId>0){
            BigDecimal transferAmount= consoleService.promptForBigDecimal("Enter amount:");
            Transfer returnedTransfer= userService.sendTransfer(userId,transferAmount);
            if(returnedTransfer!=null){
                System.out.println("\nTransaction Successfully Completed!\n" +
                        "Transaction number: "+returnedTransfer.getTransferId()+"\nReceiver: "+
                        returnedTransfer.getReceiver().getAccountUser().getUsername().toUpperCase());

            }else consoleService.printErrorMessage();

        }else consoleService.printErrorMessage();
    }
    private void transferHistoryPrinter(List<Transfer>transfers){
        if(transfers.size()>0){
            for(Transfer transfer:transfers){
                consoleService.printTransfer(transfer,currentUser);
            }
            long transferId=consoleService.promptForInt("Please enter transfer ID to view details (0 to cancel): ");
            if(transferId==0){
                return;
            }
            else if(transferId>0){
                Transfer transferFromDb=userService.getTransferById(transferId);
                if(transferFromDb!=null){
                    consoleService.promptTransferDetails(transferFromDb,currentUser);
                }else
                    consoleService.printErrorMessage();


            }else consoleService.printErrorMessage();

        }else System.out.println("You don't have any transfers");
    }
//
//	private void requestBucks() {
//        consoleService.promptForUsersHeader();
//		userService.promptAllUsers();
//        long userId= (consoleService.promptForInt("Enter ID of user you are requesting from (0 to cancel):"));
//        if(userId==0){
//            return;
//        }
//        BigDecimal transferAmount= consoleService.promptForBigDecimal("Enter amount:");
//        Transfer transfer= userService.requestTransfer(userId,transferAmount);
//
//	}


}
