package com.tpebank.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class RegisterRequest {

    @NotNull(message="Please provide first Name")
    @Size(min=1, max=50, message = "Your first name '${validatedValue}' must be between {min} and {max} chars long")
    private String firstName;

    @NotNull(message="Please provide last Name")
    @Size(min=1, max=50, message = "Your last name '${validatedValue}' must be between {min} and {max} chars long")
    private String lastName;

    //555-55-5555
    @Pattern(regexp = "^(?!000|666)[0-8][0-9]{2}-(?!00)[0-9]{2}-(?!0000)[0-9]{4}$")
    private String ssn;

    @NotNull(message="Please provide a User Name")
    @Size(min=5, max=20, message = "Your user name '${validatedValue}' must be between {min} and {max} chars long")
    private String userName;

    @Email(message="Please provide a valid email")
    @Size(min=5, max=100,message = "Your email '${validatedValue}' must be between {min} and {max} chars long")
    private String email;


    @NotNull(message="Please provide a password")
    @Size(min=5, max=20, message = "Your password '${validatedValue}' must be between {min} and {max} chars long")
    private String password;


    //555-555-5555
    //(555)-555-5555
    @Pattern(regexp = "^((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$",
            message = "Please provide valid phone number")
    private String phoneNumber;


    @NotNull(message="Please provide a address")
    @Size(min=5, max=200, message = "Your address '${validatedValue}' must be between {min} and {max} chars long")
    private String address;

    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern = "MM/dd/yyyy")
    private String dateOfBirth;
}
