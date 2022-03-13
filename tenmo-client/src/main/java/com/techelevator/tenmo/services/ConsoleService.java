package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.UserCredentials;

import java.math.BigDecimal;
import java.util.Scanner;

public class ConsoleService {

    private final Scanner scanner = new Scanner(System.in);

    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        System.out.print(prompt);
        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printGreeting() {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");
    }

    public void printLoginMenu() {
        System.out.println();
        System.out.println("1: Register");
        System.out.println("2: Login");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printMainMenu() {
        System.out.println();
        System.out.println("1: View your current balance");
        System.out.println("2: View your past transfers");
        System.out.println("3: View your pending requests");
        System.out.println("4: Send TE bucks");
        System.out.println("5: Request TE bucks");
        System.out.println("0: Exit");
        System.out.println();
    }
    public void promptForUsersHeader(){
        System.out.println("-------------------------------------------\n" +
                "Users\n" +
                "ID          Name\n" +
                "-------------------------------------------");
    }
    public void promptForListTransfers(){
        System.out.println("-------------------------------------------\n" +
                "Transfers\n" +
                "ID          From/To                 Amount\n" +
                "-------------------------------------------");
    }
    public void promptForTransferDetails(){
        System.out.println("--------------------------------------------\n" +
            "Transfer Details\n" +
            "--------------------------------------------");
    }
    public void promptForPendingRequests(){
        System.out.println("-------------------------------------------\n" +
                "Pending Transfers\n" +
                "ID          To                     Amount\n" +
                "-------------------------------------------");
    }
    public void promptForAcceptOrReject(){
        System.out.println("1: Approve\n" +
                "2: Reject\n" +
                "0: Don't approve or reject\n" +
                "---------");
    }

    public UserCredentials promptForCredentials() {
        String username = promptForString("Username: ");
        String password = promptForString("Password: ");
        return new UserCredentials(username, password);
    }

    public String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int promptForInt(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    public BigDecimal promptForBigDecimal(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a decimal number.");
            }
        }
    }



    public void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void printErrorMessage() {
        System.out.println("An error occurred. Check the log for details.");
    }
        public  void printTransfer(Transfer transfer, AuthenticatedUser currentUser){
        String senderName=transfer.getSender().getAccountUser().getUsername();
        String receiverName=transfer.getReceiver().getAccountUser().getUsername();
        String formatted;
        if(senderName.equals(currentUser.getUser().getUsername())){
            formatted = String.format("%-10d   To: %-17s $%.2f", transfer.getTransferId(), receiverName.toUpperCase(),
                    transfer.getAmount());
        }
        else{
            formatted = String.format("%-10d From: %-17s $%.2f", transfer.getTransferId(), senderName.toUpperCase(),
                    transfer.getAmount());

        }System.out.println(formatted);
    }
    public void promptTransferDetails(Transfer transfer, AuthenticatedUser currentUser){
//        Transfer expectedTransfer=getTransferById(transferId);
        String senderName=transfer.getSender().getAccountUser().getUsername();
        String receiverName=transfer.getReceiver().getAccountUser().getUsername();
        String currentUserName=currentUser.getUser().getUsername();

        System.out.printf("%-8s %d %n","Id:",transfer.getTransferId()) ;
        if(senderName.equals(currentUserName)){
            System.out.printf("%-8s %s %n","From Me:",senderName.toUpperCase()) ;
            System.out.printf("%-8s %s %n","To:",receiverName.toUpperCase()) ;
            System.out.printf("%-8s %s %n","Type:",transfer.getType().getTransferType()) ;


        }else
        {
            System.out.printf("%-8s %s %n","From:",senderName.toUpperCase()) ;
            System.out.printf("%-8s %s %n","To Me:",receiverName.toUpperCase()) ;
            System.out.printf("%-8s %s %n","Type:","Request") ;

       }
        System.out.printf("%-8s %s %n","Status:",transfer.getStatus().getTransferStatus());
       System.out.printf("%-8s $%.2f %n","Amount:",transfer.getAmount());


   }

}
