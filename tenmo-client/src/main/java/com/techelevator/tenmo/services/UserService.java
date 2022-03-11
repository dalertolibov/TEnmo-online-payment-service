package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.*;
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
        ResponseEntity<BigDecimal> response=restTemplate.exchange(baseUrl+"balance",
                HttpMethod.GET,makeAuthEntity(),BigDecimal.class);
        return response.getBody();
    }
    public User[] getAllUsers (){
        ResponseEntity<User[]>response=restTemplate.exchange(baseUrl+"users",HttpMethod.GET,makeAuthEntity(),User[].class);
        return response.getBody();
    }
    public Transfer[] getAllTransfer() {
        ResponseEntity<Transfer[]>response=restTemplate.exchange(baseUrl+"transfers",HttpMethod.GET,makeAuthEntity(),Transfer[].class);
        return response.getBody();
    }
    public Account getAccountByUserId(Long userId){
        ResponseEntity<Account> response=restTemplate.exchange(baseUrl+"accounts/"+userId,
                HttpMethod.GET,makeAuthEntity(),Account.class);
        return response.getBody();
    }
    public Transfer sendTransfer(Long receiverId,BigDecimal transferAmount){
        Transfer transfer=new Transfer();
        transfer.setTransferStatusId(2L);
        transfer.setTransferTypeId(1L);
        transfer.setAccountFrom(getAccountByUserId(currentUser.getUser().getId()));
        transfer.setAccountTo(getAccountByUserId(receiverId));
        transfer.setAmount(transferAmount);
      ResponseEntity<Transfer>response=restTemplate.exchange(baseUrl+"transfers",HttpMethod.POST,
             makeTransferEntity(transfer),Transfer.class );
      return response.getBody();


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
