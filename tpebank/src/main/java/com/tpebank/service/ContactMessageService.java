package com.tpebank.service;

import com.tpebank.domain.ContactMessage;
import com.tpebank.dto.ContactMessageDTO;
import com.tpebank.exception.ResourceNotFoundException;
import com.tpebank.exception.message.ExceptionMessages;
import com.tpebank.repository.ContactMessageRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.naming.CompositeName;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ContactMessageService {

    private ContactMessageRepository contactMessageRepository;

    private ModelMapper modelMapper;

    private ContactMessage convertTo(ContactMessageDTO contactMessageDTO){
       ContactMessage contactMessage= modelMapper.map(contactMessageDTO,ContactMessage.class);
       return contactMessage;
    }

    private ContactMessageDTO convertToDTO(ContactMessage contactMessage){
        ContactMessageDTO contactMessageDTO= modelMapper.map(contactMessage,ContactMessageDTO.class);
        return contactMessageDTO;
    }

    public void saveMessage(ContactMessageDTO contactMessageDTO){
        ContactMessage contactMessage= convertTo(contactMessageDTO);
        contactMessageRepository.save(contactMessage);
    }

    public List<ContactMessageDTO> getAll(){
        List<ContactMessage> messages=contactMessageRepository.findAll();
        return messages.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private ContactMessage getById(Long id){
        return   contactMessageRepository.findById(id).
                orElseThrow(()->new ResourceNotFoundException(String.format(ExceptionMessages.CONTACTMESSAGE_NOT_FOUND_MESSAGE,id)));
    }
    public ContactMessageDTO getMessage(Long id){
        ContactMessage contactMessage= getById(id);
        return convertToDTO(contactMessage);
    }


    public void deleteMessage(Long id){
        ContactMessage contactMessage= getById(id);
        contactMessageRepository.deleteById(contactMessage.getId());
    }

    public void updateMessage(Long id, ContactMessageDTO contactMessageDTO){
        ContactMessage foundMessage= getById(id);

        foundMessage.setName(contactMessageDTO.getName());
        foundMessage.setEmail(contactMessageDTO.getEmail());
        foundMessage.setPhoneNumber(contactMessageDTO.getPhoneNumber());
        foundMessage.setSubject(contactMessageDTO.getSubject());
        foundMessage.setBody(contactMessageDTO.getBody());

        contactMessageRepository.save(foundMessage);
    }

    public Page<ContactMessageDTO> getMessagePage(Pageable pageable){
       Page<ContactMessage> messages=  contactMessageRepository.findAll(pageable);

      Page<ContactMessageDTO> dtoPage=  messages.map(new Function<ContactMessage, ContactMessageDTO>() {
           @Override
           public ContactMessageDTO apply(ContactMessage contactMessage) {
               return convertToDTO(contactMessage);
           }
       });

      return dtoPage;
    }
}
