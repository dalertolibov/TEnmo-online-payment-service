package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.*;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class UserService {



    private final String baseUrl;
    private final RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser currentUser;


    public UserService(String baseUrl) {
        this.baseUrl=baseUrl;
    }




    public BigDecimal getBalance(){
        BigDecimal balance=null;
       try{
           ResponseEntity<BigDecimal> response=restTemplate.exchange(baseUrl+"balance",
                   HttpMethod.GET,makeAuthEntity(),BigDecimal.class);
           balance=response.getBody();
       } catch (RestClientResponseException ex) {
           System.out.println("Request - Responce error: " + ex.getRawStatusCode());
       } catch (ResourceAccessException e) {
           System.out.println("Server not accessible. Check your connection or try again.");
       }
       return balance;

    }
    public void promptAllUsers(){
        for(User user:getAllUsers()){
            String formattedString=String.format("%-10d %s",user.getId(),user.getUsername().toUpperCase());
            System.out.println(formattedString);
        }
    }


    public User[] getAllUsers (){
        User[]allUsers=null;
        try{
            ResponseEntity<User[]>response=restTemplate.exchange(baseUrl+"users",HttpMethod.GET,makeAuthEntity(),User[].class);

        allUsers= response.getBody();
        } catch (RestClientResponseException ex) {
            System.out.println("Request - Responce error: " + ex.getRawStatusCode());
        } catch (ResourceAccessException e) {
            System.out.println("Server not accessible. Check your connection or try again.");
        }
        return allUsers;
    }

    public void promptForApprovedTransfers(){

        for(Transfer transfer:getAllTransfer()){
            boolean isTransferApproved=transfer.getStatus().getTransferStatus().equals("Approved");
            if(isTransferApproved){
                printTransfer(transfer);
            }
        }
    }
    public void promptForPendingTransfers(){
        for(Transfer transfer:getAllTransfer()){
            boolean isTransferPending=transfer.getStatus().getTransferStatus().equals("Pending");
            if(isTransferPending){
                printTransfer(transfer);

            }
        }
    }



    public Transfer[] getAllTransfer() {
        Transfer[]allTransfers=null;
        try{
        ResponseEntity<Transfer[]>response=restTemplate.exchange(baseUrl+"transfers",HttpMethod.GET,makeAuthEntity(),Transfer[].class);
        allTransfers= response.getBody();
        } catch (RestClientResponseException ex) {
            System.out.println("Request - Responce error: " + ex.getRawStatusCode());
        } catch (ResourceAccessException e) {
            System.out.println("Server not accessible. Check your connection or try again.");
        }
        return allTransfers;
    }
    public void promptTransferDetails(Long transferId){
        Transfer expectedTransfer=getTransferById(transferId);

        String senderName=expectedTransfer.getSender().getAccountUser().getUsername();
        String receiverName=expectedTransfer.getReceiver().getAccountUser().getUsername();
        String currentUserName=currentUser.getUser().getUsername();

        System.out.printf("Id:%12d %n",expectedTransfer.getTransferId()) ;
        if(senderName.equals(currentUserName)){

        System.out.printf("From Me:%8s %nTo:%12s %nType:%10s %n",senderName.toUpperCase(),
                receiverName.toUpperCase(),expectedTransfer.getType().getTransferType());


        }else
        {
            System.out.printf("From:%12s %nTo Me:%11s %nType:%10s %n",senderName.toUpperCase(),receiverName.toUpperCase(),"Request");
        }
        System.out.printf("Status:%12s %n",

                expectedTransfer.getStatus().getTransferStatus());
        System.out.println("Amount:    $"+expectedTransfer.getAmount());


    }
    public Transfer getTransferById(Long transferId){

            ResponseEntity<Transfer>response=restTemplate.exchange(baseUrl+"transfers/"+transferId,
                    HttpMethod.GET,makeAuthEntity(),Transfer.class);
            return  response.getBody();
    }
    public Account getAccountByUserId(Long userId){
        Account account=null;
        try{
        ResponseEntity<Account> response=restTemplate.exchange(baseUrl+"accounts/"+userId,
                HttpMethod.GET,makeAuthEntity(),Account.class);
        account= response.getBody();
        } catch (RestClientResponseException ex) {
            System.out.println("Request - Responce error: " + ex.getRawStatusCode());
        } catch (ResourceAccessException e) {
            System.out.println("Server not accessible. Check your connection or try again.");
        }
        return account;
    }


    public Transfer sendTransfer(Long receiverId,BigDecimal transferAmount){

        Transfer transfer=new Transfer();
        transfer.setSender(getAccountByUserId(currentUser.getUser().getId()));
        transfer.setReceiver(getAccountByUserId(receiverId));
        transfer.setAmount(transferAmount);
        Transfer expected =null;
        try{
            ResponseEntity<Transfer>response=restTemplate.exchange(baseUrl+"transfers",HttpMethod.POST,
             makeTransferEntity(transfer),Transfer.class );
         expected= response.getBody();
        } catch (RestClientResponseException ex) {
            System.out.println("Request - Responce error: " + ex.getRawStatusCode());
        } catch (ResourceAccessException e) {
            System.out.println("Server not accessible. Check your connection or try again.");
        }
        return expected;
    }
    public Transfer requestTransfer(Long receiverId,BigDecimal transferAmount){

        Transfer transfer=new Transfer();
        transfer.setSender(getAccountByUserId(receiverId));
        transfer.setReceiver(getAccountByUserId(currentUser.getUser().getId()));
        transfer.setAmount(transferAmount);
        Transfer expected =null;
        try{
            ResponseEntity<Transfer>response=restTemplate.exchange(baseUrl+"transfers",HttpMethod.POST,
                    makeTransferEntity(transfer),Transfer.class );
            expected= response.getBody();
        } catch (RestClientResponseException ex) {
            System.out.println("Request - Responce error: " + ex.getRawStatusCode());
        } catch (ResourceAccessException e) {
            System.out.println("Server not accessible. Check your connection or try again.");
        }
        return expected;
    }


    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(currentUser.getToken());
        return new HttpEntity<>(transfer, headers);
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        return new HttpEntity<>(headers);
    }

    public void setCurrentUser(AuthenticatedUser currentUser) {
        this.currentUser = currentUser;
    }


    private void printTransfer(Transfer transfer){
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
}
