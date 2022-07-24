package com.tpebank.controller;


import com.tpebank.dto.request.LoginRequest;
import com.tpebank.dto.request.RegisterRequest;
import com.tpebank.dto.response.LoginResponse;
import com.tpebank.dto.response.ResponseMessages;
import com.tpebank.dto.response.TpeResponse;
import com.tpebank.security.JwtUtils;
import com.tpebank.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping
@AllArgsConstructor
public class UserJWTController {

    private UserService userService;

    private AuthenticationManager authenticationManager;

    private JwtUtils jwtUtils;



    /*
    {
    "firstName": "john",
    "lastName": "coffee",
    "userName":"johnuser",
    "password": "password",
    "phoneNumber": "(541) 317-8828",
    "email": "admin@email.com",
    "address": "NewYork,USA",
    "ssn":"333-44-4444",
    "dateOfBirth":"05/01/2000"

}

//localhost:8080/register
     */
    @PostMapping("/register")
    public ResponseEntity<TpeResponse> register(@Valid @RequestBody RegisterRequest registerRequest){
           userService.registerUser(registerRequest);
           TpeResponse response=new TpeResponse(true, ResponseMessages.REGISTER_RESPONSE_MESSAGE);
           return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /*
    {
    "userName":"johnuser",
    "password":"password"
}
     */
    //localhost:8080/login
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest){
      Authentication authentication= authenticationManager.authenticate
                 (new UsernamePasswordAuthenticationToken(loginRequest.getUserName(),loginRequest.getPassword()));
      String token= jwtUtils.generateJwtToken(authentication);
      LoginResponse response=new LoginResponse(token);
      return ResponseEntity.ok(response);
    }


}
