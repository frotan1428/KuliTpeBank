package com.tpebank.controller;

import com.tpebank.dto.UserDTO;
import com.tpebank.dto.request.UserUpdateRequest;
import com.tpebank.dto.response.ResponseMessages;
import com.tpebank.dto.response.TpeResponse;
import com.tpebank.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")

@AllArgsConstructor
public class UserController {

    private UserService userService;

    //localhost:8080/user/info
    @GetMapping("/info")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    public ResponseEntity<UserDTO> getUserInfo(){
         UserDTO userDTO= userService.findUserByUserName();
         return ResponseEntity.ok(userDTO);
    }

//http://localhost:8080/user/all?page=0&size=2&sort=id&direction=DESC
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserDTO>> getUserPage(@RequestParam("page") int page,
                                                     @RequestParam("size") int size,
                                                     @RequestParam("sort") String prop,
                                                     @RequestParam("direction") Sort.Direction direction){

        Pageable pageable= PageRequest.of(page,size,Sort.by(direction,prop));
        Page<UserDTO> userDTOPage= userService.getUsers(pageable);

        return ResponseEntity.ok(userDTOPage);

    }


    //  localhost:8080/user/2

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id){
        UserDTO userDTO= userService.getUserById(id);
        return ResponseEntity.ok(userDTO);
    }



    /*
    {
    "firstName": "bruce-update",
    "lastName": "wayne-update",
    "password": "password",
    "phoneNumber": "(541) 317-8828",
    "email": "bruce@email.com",
    "address": "NewYork,USA",
    "ssn":"555-44-4444",
    "dateOfBirth":"05/01/2000",
    "enabled":false,
    "roles": [
        "Admin","Customer1"
    ]

}
    localhost:8080/user/2
     */

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TpeResponse> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateRequest userUpdateRequest){
        userService.updateUser(id,userUpdateRequest);

        TpeResponse response=new TpeResponse(true, ResponseMessages.USER_UPDATE_RESPONSE_MESSAGE);
        return ResponseEntity.ok(response);
    }




}
