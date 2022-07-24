package com.tpebank.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class LoginRequest {

    @NotNull(message="Please provide a username")
    private String userName;

    @NotNull(message="Please provide a password")
    private String password;
}
