package com.tpebank.service;

import com.tpebank.domain.Role;
import com.tpebank.domain.User;
import com.tpebank.domain.enums.RoleType;
import com.tpebank.dto.UserDTO;
import com.tpebank.dto.request.RegisterRequest;
import com.tpebank.dto.request.UserUpdateRequest;
import com.tpebank.exception.ConflictException;
import com.tpebank.exception.ResourceNotFoundException;
import com.tpebank.exception.message.ExceptionMessages;
import com.tpebank.repository.RoleRepository;
import com.tpebank.repository.UserRepository;
import com.tpebank.security.SecurityUtils;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Service
public class UserService {

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private PasswordEncoder passwordEncoder;

    private AccountService accountService;

    private ModelMapper modelMapper;


    @Transactional
    public void registerUser(RegisterRequest registerRequest){
       if(userRepository.existsByUserName(registerRequest.getUserName())){
           throw  new ConflictException(String.format(ExceptionMessages.USERNAME_ALREADY_EXIST_MESSAGE,registerRequest.getUserName()));
        }

        if(userRepository.existsByEmail(registerRequest.getEmail())){
            throw  new ConflictException(String.format(ExceptionMessages.EMAIL_ALREADY_EXIST_MESSAGE,registerRequest.getEmail()));
        }

        if(userRepository.existsBySsn(registerRequest.getSsn())){
            throw  new ConflictException(String.format(ExceptionMessages.SSN_ALREADY_EXIST_MESSAGE,registerRequest.getSsn()));
        }

        Set<Role> userRoles=new HashSet<>();


        Role role=roleRepository.findByName(RoleType.ROLE_CUSTOMER).
                orElseThrow(()-> new ResourceNotFoundException(String.format(ExceptionMessages.
                        ROLE_NOT_FOUND_MESSAGE,RoleType.ROLE_CUSTOMER.name())));

       userRoles.add(role);

       User user= new User();
       user.setFirstName(registerRequest.getFirstName());
       user.setLastName(registerRequest.getLastName());
       user.setSsn(registerRequest.getSsn());
       user.setUserName(registerRequest.getUserName());
       user.setEmail(registerRequest.getEmail());
       user.setPhoneNumber(registerRequest.getPhoneNumber());
       user.setAddress(registerRequest.getAddress());
       user.setDateOfBirth(registerRequest.getDateOfBirth());
       user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
       user.setRoles(userRoles);

       userRepository.save(user);


       accountService.createAccount(user);
    }

    public UserDTO findUserByUserName(){
       String currentUserName= SecurityUtils.getCurrentUserLogin().orElseThrow(()->
                new ResourceNotFoundException(ExceptionMessages.CURRENTUSER_NOT_FOUND_MESSAGE));


        User user= userRepository.findOneWithAuthoritiesByUserName(currentUserName).orElseThrow(()->
                new ResourceNotFoundException(String.format(ExceptionMessages.USER_NOT_FOUND_MESSAGE,currentUserName)));

       return convertToDTO(user);
    }

    public Page<UserDTO> getUsers(Pageable pageable){
          return userRepository.findUserPage(pageable);
    }

    public User getById(Long id){
       return userRepository.findById(id).orElseThrow(()->new
                ResourceNotFoundException(String.format(ExceptionMessages.USERID_NOT_FOUND_MESSAGE,id)));
    }

    public UserDTO getUserById(Long id){
        User user= getById(id);
        return convertToDTO(user);
    }

    public void updateUser(Long id, UserUpdateRequest userUpdateRequest){
          User foundUser= getById(id);

          boolean emailExist= userRepository.existsByEmail(userUpdateRequest.getEmail());

          if(emailExist &&!foundUser.getEmail().equals(userUpdateRequest.getEmail())){
              throw new ConflictException(String.format(ExceptionMessages.EMAIL_ALREADY_EXIST_MESSAGE,userUpdateRequest.getEmail()));
          }

          boolean ssnExist= userRepository.existsBySsn(userUpdateRequest.getSsn());

        if(ssnExist &&!foundUser.getSsn().equals(userUpdateRequest.getSsn())){
            throw new ConflictException(String.format(ExceptionMessages.SSN_ALREADY_EXIST_MESSAGE,userUpdateRequest.getSsn()));
        }

        foundUser.setFirstName(userUpdateRequest.getFirstName());
        foundUser.setLastName(userUpdateRequest.getLastName());
        foundUser.setEmail(userUpdateRequest.getEmail());
        foundUser.setDateOfBirth(userUpdateRequest.getDateOfBirth());
        foundUser.setPhoneNumber(userUpdateRequest.getPhoneNumber());
        foundUser.setSsn(userUpdateRequest.getSsn());
        foundUser.setAddress(userUpdateRequest.getAddress());

        if(userUpdateRequest.getEnabled()!=null){
            foundUser.setEnabled(userUpdateRequest.getEnabled());
        }

        Set<Role> roles= getRoles(userUpdateRequest.getRoles());
        foundUser.setRoles(roles);

        userRepository.save(foundUser);
    }


    private Set<Role> getRoles(Set<String> userStrRoles){
        Set<Role> roles=new HashSet<>();

        if(userStrRoles==null){
          Role role=  roleRepository.findByName(RoleType.ROLE_CUSTOMER).
                    orElseThrow(()->new ResourceNotFoundException(String.format(ExceptionMessages.
                            ROLE_NOT_FOUND_MESSAGE,RoleType.ROLE_CUSTOMER.name())));

          roles.add(role);
        }else{
            userStrRoles.forEach(r->{
                switch (r){
                    case "Admin":
                        Role adminRole=  roleRepository.findByName(RoleType.ROLE_ADMIN).
                                orElseThrow(()->new ResourceNotFoundException(String.format(ExceptionMessages.
                                        ROLE_NOT_FOUND_MESSAGE,RoleType.ROLE_ADMIN.name())));
                        roles.add(adminRole);
                        break;

                    default:
                        Role userRole=  roleRepository.findByName(RoleType.ROLE_CUSTOMER).
                                orElseThrow(()->new ResourceNotFoundException(String.format(ExceptionMessages.
                                        ROLE_NOT_FOUND_MESSAGE,RoleType.ROLE_CUSTOMER.name())));

                        roles.add(userRole);

                }
            });
        }

        return roles;

    }

    public User getUserByUserName(String userName){
        return userRepository.findByUserName(userName).orElseThrow(()->
                new ResourceNotFoundException(String.format(ExceptionMessages.USER_NOT_FOUND_MESSAGE,userName)));
    }


    private UserDTO convertToDTO(User user){
        UserDTO userDTO=modelMapper.map(user,UserDTO.class);
        return userDTO;
    }



}
