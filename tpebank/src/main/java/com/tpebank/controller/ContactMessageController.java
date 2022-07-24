package com.tpebank.controller;

import com.tpebank.domain.ContactMessage;
import com.tpebank.dto.ContactMessageDTO;
import com.tpebank.dto.response.ResponseMessages;
import com.tpebank.dto.response.TpeResponse;
import com.tpebank.service.ContactMessageService;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/message")
//@AllArgsConstructor
public class ContactMessageController {

    private ContactMessageService contactMessageService;

    public ContactMessageController(ContactMessageService contactMessageService){
        this.contactMessageService=contactMessageService;
    }
 /*
    {
        "name":"Bruce",
            "email":"Bruce@email.com",
            "phoneNumber":"555-444-5555",
            "subject":"Hi, What is up",
            "body":"I have a problem about transaction"
    }

    localhost:8080/message/visitor
    */
    @PostMapping("/visitor")
    public ResponseEntity<TpeResponse> createMessage(@Valid @RequestBody ContactMessageDTO contactMessageDTO){
        contactMessageService.saveMessage(contactMessageDTO);
        TpeResponse tResponse=new TpeResponse(true, ResponseMessages.CONTACTMESSAGE_SAVE_RESPONSE_MESSAGE);
        return new ResponseEntity<>(tResponse, HttpStatus.CREATED);
    }

    //localhost:8080/message
    @GetMapping
     public ResponseEntity<List<ContactMessageDTO>> getAllMessages(){
        List<ContactMessageDTO> messages= contactMessageService.getAll();
        return ResponseEntity.ok(messages);
    }

    //localhost:8080/message/1
    @GetMapping("/{id}")
    public ResponseEntity<ContactMessageDTO> getMessage(@PathVariable Long id){
        ContactMessageDTO messageDTO= contactMessageService.getMessage(id);
        return ResponseEntity.ok(messageDTO);
    }

    //localhost:8080/message/1
    @DeleteMapping("/{id}")
    public ResponseEntity<TpeResponse> deleteMessage(@PathVariable Long id){
        contactMessageService.deleteMessage(id);
        TpeResponse tResponse=new TpeResponse(true,ResponseMessages.CONTACTMESSAGE_DELETE_RESPONSE_MESSAGE);
        return ResponseEntity.ok(tResponse);
    }


    /*
     /*
    {
            "name":"Bruce",
            "email":"Bruce@email.com",
            "phoneNumber":"555-444-5555",
            "subject":"Hi, What is up",
            "body":"I have a problem about transaction"
    }

    localhost:8080/message/1
    */


    @PutMapping("/{id}")
    public ResponseEntity<TpeResponse> updateMessage(@PathVariable Long id,@Valid @RequestBody ContactMessageDTO contactMessageDTO ){
        contactMessageService.updateMessage(id,contactMessageDTO);
        TpeResponse tResponse=new TpeResponse(true,ResponseMessages.CONTACTMESSAGE_UPDATE_RESPONSE_MESSAGE);
        return ResponseEntity.ok(tResponse);
    }


    //http://localhost:8080/message/pages?page=0&size=2&sort=id&direction=ASC
    @GetMapping("/pages")
    public ResponseEntity<Page<ContactMessageDTO>> getMessagePage(@RequestParam ("page") int page,
                                                                  @RequestParam("size") int size,
                                                                  @RequestParam("sort") String prop,
                                                                  @RequestParam("direction") Sort.Direction direction){

        Pageable pageable= PageRequest.of(page,size,Sort.by(direction,prop));
        Page<ContactMessageDTO> messagePage= contactMessageService.getMessagePage(pageable);
        return ResponseEntity.ok(messagePage);
    }

}
