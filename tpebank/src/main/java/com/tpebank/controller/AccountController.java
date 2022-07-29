package com.tpebank.controller;

import com.tpebank.domain.User;
import com.tpebank.dto.request.RecipientRequest;
import com.tpebank.dto.request.TransactionRequest;
import com.tpebank.dto.request.TransferRequest;
import com.tpebank.dto.response.*;
import com.tpebank.exception.ResourceNotFoundException;
import com.tpebank.exception.message.ExceptionMessages;
import com.tpebank.security.SecurityUtils;
import com.tpebank.service.AccountService;
import com.tpebank.service.RecipientService;
import com.tpebank.service.TransactionService;
import com.tpebank.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

@RestController
@RequestMapping("/account")
@AllArgsConstructor
public class AccountController {

 private RecipientService recipientService;

 private AccountService accountService;

 private UserService userService;

 private TransactionService transactionService;

 /*
 {
    "name":"bruce-update wayne-update",
    "accountNumber":3602359738392410
}
localhost:8080/account/recipient
  */

@PostMapping("/recipient")
 @PreAuthorize("hasRole('CUSTOMER')")
 public ResponseEntity<TpeResponse> addRecipient(@Valid @RequestBody RecipientRequest recipientRequest){
  recipientService.addRecipient(recipientRequest);
  TpeResponse response=new TpeResponse(true, ResponseMessages.RECIPIENT_SAVE_RESPONSE_MESSAGE);
  return new ResponseEntity<>(response, HttpStatus.CREATED);
}

//localhost:8080/account/recipient
@GetMapping("/recipient")
@PreAuthorize("hasRole('CUSTOMER')")
 public ResponseEntity<RecipientListResponse> getRecipient(){
    RecipientListResponse recipientListResponse=  recipientService.getRecipients();
    return ResponseEntity.ok(recipientListResponse);
}

    //localhost:8080/account/recipient/1
@PreAuthorize("hasRole('CUSTOMER')")
@DeleteMapping("/recipient/{id}")
public ResponseEntity<TpeResponse> deleteRecipient(@PathVariable Long id){
           recipientService.deleteRecipient(id);
    TpeResponse response=new TpeResponse(true, ResponseMessages.RECIPIENT_DELETE_RESPONSE_MESSAGE);
    return new ResponseEntity<>(response, HttpStatus.OK);

}

  /*
    {
    "amount":2550,
    "comment":"It is a deposit"
}
     */


@PostMapping("/deposit")
@PreAuthorize("hasRole('CUSTOMER')")
public ResponseEntity<TpeResponse> deposit(@Valid @RequestBody TransactionRequest transactionRequest){

    String userName= SecurityUtils.getCurrentUserLogin().orElseThrow(()->new
            ResourceNotFoundException(ExceptionMessages.CURRENTUSER_NOT_FOUND_MESSAGE));

    User user= userService.getUserByUserName(userName);


    accountService.deposit(transactionRequest,user);

    TpeResponse response=new TpeResponse(true, ResponseMessages.DEPOSIT_RESPONSE_MESSAGE);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
}

  /*
    {
    "amount":2550,
    "comment":"It is a deposit"
}
     */

    @PostMapping("/withdraw")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<TpeResponse> withdraw(@Valid @RequestBody TransactionRequest transactionRequest){

        String userName= SecurityUtils.getCurrentUserLogin().orElseThrow(()->new
                ResourceNotFoundException(ExceptionMessages.CURRENTUSER_NOT_FOUND_MESSAGE));

        User user= userService.getUserByUserName(userName);


        accountService.withdraw(transactionRequest,user);

        TpeResponse response=new TpeResponse(true, ResponseMessages.WITHDRAW_RESPONSE_MESSAGE);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    /*
    {
    "recipientNumber":1549788318050308,
    "amount":2550,
    "comment":"It is a deposit"
}
     */
    @PostMapping("/transfer")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<TpeResponse> transfer(@Valid @RequestBody TransferRequest transferRequest){

        String userName= SecurityUtils.getCurrentUserLogin().orElseThrow(()->new
                ResourceNotFoundException(ExceptionMessages.CURRENTUSER_NOT_FOUND_MESSAGE));

        User user= userService.getUserByUserName(userName);

        accountService.transfer(transferRequest,user);

        TpeResponse response=new TpeResponse(true, ResponseMessages.TRANSFER_RESPONSE_MESSAGE);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @GetMapping("/bankstatement")
    @PreAuthorize("hasRole('ADMIN')")
    //2022-07-28
    public ResponseEntity<BankStatementResponse> getBankStatement
             (@RequestParam(value="startDate") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate startDate,
             @RequestParam(value="endDate") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate endDate) {

        BankStatementResponse response=  transactionService.calculateBankStatement(startDate,endDate);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/customerstatement")
    @PreAuthorize("hasRole('CUSTOMER')")
    //2022-07-28
    public ResponseEntity<CustomerStatementResponse> getCustomerStatement
            (@RequestParam(value="startDate") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate startDate,
             @RequestParam(value="endDate") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate endDate) {

        String userName= SecurityUtils.getCurrentUserLogin().orElseThrow(()->new
                ResourceNotFoundException(ExceptionMessages.CURRENTUSER_NOT_FOUND_MESSAGE));

        User user= userService.getUserByUserName(userName);

        CustomerStatementResponse response=  transactionService.calculateCustomerStatement(startDate,endDate,user);
        return ResponseEntity.ok(response);
    }




}
