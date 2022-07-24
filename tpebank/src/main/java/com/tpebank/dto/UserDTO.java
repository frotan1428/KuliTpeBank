package com.tpebank.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tpebank.domain.Role;
import com.tpebank.domain.User;
import com.tpebank.domain.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;

    private String firstName;

    private String lastName;

    private String ssn;


    private String userName;

    private String email;

    @JsonIgnore
    private String password;

    private String phoneNumber;

    private String address;

    private Boolean enabled;

    private String dateOfBirth;

    private Set<Role> roles;

    public Set<String> getRoles(){
        Set<String> roleStr=new HashSet<>();

        roles.forEach(r->{
            if(r.getName().equals(RoleType.ROLE_ADMIN))
                    roleStr.add("Admin");
                else
                    roleStr.add("Customer");
        });

        return roleStr;
    }

    public UserDTO(User user){
        this.id=user.getId();
        this.firstName=user.getFirstName();
        this.lastName=user.getLastName();
        this.ssn=user.getSsn();
        this.userName=user.getUserName();
        this.email=user.getEmail();
        this.phoneNumber=user.getPhoneNumber();
        this.address=user.getAddress();
        this.enabled=user.getEnabled();
        this.dateOfBirth=user.getDateOfBirth();
        this.roles=user.getRoles();
    }




}
