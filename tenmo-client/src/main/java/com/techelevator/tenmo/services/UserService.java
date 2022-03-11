package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

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
      //Need to make private method to create transfer
        Transfer transfer=new Transfer();
        transfer.setTransferStatusId(2L);
        transfer.setTransferTypeId(1L);
        transfer.setAccountFrom(getAccountByUserId(currentUser.getUser().getId()));
        transfer.setAccountTo(getAccountByUserId(receiverId));
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
}
