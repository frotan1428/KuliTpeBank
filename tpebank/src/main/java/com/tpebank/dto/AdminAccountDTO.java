package com.tpebank.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminAccountDTO {
    private String firstName;
    private String lastName;
    private String ssn;
    private String email;
    private String phoneNumber;
    private Long accountNumber;
    private BigDecimal accountBalance;
}
