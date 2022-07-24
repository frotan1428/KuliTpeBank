package com.tpebank.controller;

import com.tpebank.dto.request.RecipientRequest;
import com.tpebank.dto.response.RecipientListResponse;
import com.tpebank.dto.response.ResponseMessages;
import com.tpebank.dto.response.TpeResponse;
import com.tpebank.service.RecipientService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/account")
@AllArgsConstructor
public class AccountController {

 private RecipientService recipientService;

 /*
 {
    "name":"bruce-update wayne-update",
    "accountNumber":3602359738392410
}
localhost:8080/account/recipient
  */

@PostMapping("/recipient")
 @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
 public ResponseEntity<TpeResponse> addRecipient(@Valid @RequestBody RecipientRequest recipientRequest){
  recipientService.addRecipient(recipientRequest);
  TpeResponse response=new TpeResponse(true, ResponseMessages.RECIPIENT_SAVE_RESPONSE_MESSAGE);
  return new ResponseEntity<>(response, HttpStatus.CREATED);
}

//localhost:8080/account/recipient
@GetMapping("/recipient")
@PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
 public ResponseEntity<RecipientListResponse> getRecipient(){
    RecipientListResponse recipientListResponse=  recipientService.getRecipients();
    return ResponseEntity.ok(recipientListResponse);
}



}
