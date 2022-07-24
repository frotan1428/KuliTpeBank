package com.tpebank.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {

    @NotNull(message="Please provide first Name")
    @Size(min=1, max=50, message = "Your first name '${validatedValue}' must be between {min} and {max} chars long")
    private String firstName;

    @NotNull(message="Please provide last Name")
    @Size(min=1, max=50, message = "Your last name '${validatedValue}' must be between {min} and {max} chars long")
    private String lastName;

    //555-55-5555
    @Pattern(regexp = "^(?!000|666)[0-8][0-9]{2}-(?!00)[0-9]{2}-(?!0000)[0-9]{4}$")
    private String ssn;

    @Email(message="Please provide a valid email")
    @Size(min=5, max=100,message = "Your email '${validatedValue}' must be between {min} and {max} chars long")
    private String email;


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

    private Set<String> roles;

    private Boolean enabled;


}
