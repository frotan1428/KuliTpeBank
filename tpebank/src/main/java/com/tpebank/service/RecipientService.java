package com.tpebank.service;


import com.tpebank.domain.Account;
import com.tpebank.domain.Recipient;
import com.tpebank.domain.User;
import com.tpebank.dto.RecipientDTO;
import com.tpebank.dto.request.RecipientRequest;
import com.tpebank.dto.response.RecipientListResponse;
import com.tpebank.exception.ConflictException;
import com.tpebank.exception.ResourceNotFoundException;
import com.tpebank.exception.message.ExceptionMessages;
import com.tpebank.repository.RecipientRepository;
import com.tpebank.security.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RecipientService {

    private RecipientRepository recipientRepository;

    private UserService userService;

    private AccountService accountService;

    public void addRecipient(RecipientRequest recipientRequest){
         String userName= SecurityUtils.getCurrentUserLogin().orElseThrow(()->new
                 ResourceNotFoundException(ExceptionMessages.CURRENTUSER_NOT_FOUND_MESSAGE));

         User user= userService.getUserByUserName(userName);

        Account account=accountService.findByAccountNumber(recipientRequest.getAccountNumber());

        if(user.getId().equals(account.getUser().getId())){
            throw new ConflictException(ExceptionMessages.RECIPIENT_ADD_ERROR_MESSAGE);
        }


        Boolean exist= recipientRepository.existsByUserAndAccount(user,account);
        if(exist){
           throw new ConflictException(ExceptionMessages.RECIPIENT_ALREADY_EXIST_MESSAGE);
        }

        validateRecipient(recipientRequest,account);

        Recipient recipient=new Recipient();
        recipient.setUser(user);
        recipient.setAccount(account);

       recipientRepository.save(recipient);
    }

    public void deleteRecipient(Long id){
        String userName= SecurityUtils.getCurrentUserLogin().orElseThrow(()->new
                ResourceNotFoundException(ExceptionMessages.CURRENTUSER_NOT_FOUND_MESSAGE));
        User user= userService.getUserByUserName(userName);

        Recipient recipient= recipientRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException(String.format(ExceptionMessages.RECIPIENT_NOT_FOUND_MESSAGE,id)));

        if(user.getId().equals(recipient.getUser().getId())){
            recipientRepository.deleteById(recipient.getId());
        }else{
            throw new ConflictException(ExceptionMessages.RECIPIENT_DELETE_PERMISSON_MESSAGE);
        }

    }

    public RecipientListResponse getRecipients(){
        String userName= SecurityUtils.getCurrentUserLogin().orElseThrow(()->new
                ResourceNotFoundException(ExceptionMessages.CURRENTUSER_NOT_FOUND_MESSAGE));

        User user= userService.getUserByUserName(userName);

        List<Recipient> recipientList= user.getRecipients();

       List<RecipientDTO> recipientDTOList=  recipientList.stream().map(this::convertToDTO).collect(Collectors.toList());

       RecipientListResponse response=new RecipientListResponse();
       response.setRecipients(recipientDTOList);

        return response;
    }

    private void validateRecipient(RecipientRequest recipientRequest, Account account){
       String fullName= account.getUser().getFirstName()+" "+account.getUser().getLastName();

       if(!fullName.equalsIgnoreCase(recipientRequest.getName())){
           throw new ResourceNotFoundException(ExceptionMessages.RECIPIENT_VALIDATION_ERROR_MESSAGE);
       }
    }

    private RecipientDTO convertToDTO(Recipient recipient){
        RecipientDTO recipientDTO=new RecipientDTO();
        User user= recipient.getAccount().getUser();
        recipientDTO.setId(recipient.getId());
        recipientDTO.setFirstName(user.getFirstName());
        recipientDTO.setLastName(user.getLastName());
        recipientDTO.setPhoneNumber(user.getPhoneNumber());
        recipientDTO.setEmail(user.getEmail());
        recipientDTO.setAccountNumber(recipient.getAccount().getAccountNumber());
        return recipientDTO;
    }

}
