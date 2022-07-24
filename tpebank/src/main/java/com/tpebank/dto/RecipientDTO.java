package com.tpebank.dto;

import lombok.Data;
import lombok.Getter;

@Data
public class RecipientDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private Long accountNumber;

}
