package com.tpebank.dto;

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
public class ContactMessageDTO {

    @NotNull(message="Please provide your name")
    @Size(min=1, max=15, message="Your name '${validatedValue}' must be between {min} and {max} chars long")
    private String name;

    @NotNull(message="Please provide a message subject")
    @Size(min=5, max=20, message="Message subject '${validatedValue}' must be between {min} and {max} chars long")
    private String subject;


    @NotNull(message="Please provide a message body")
    @Size(min=20, max=200, message="Message Body '${validatedValue}' must be between {min} and {max} chars long")
    private String body;


    @Email
    private String email;

    //(555) 555 5555
    //555 555 5555
    //555-555-5555
    @Pattern(regexp = "^((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$",
            message = "Please provide valid phone number")
    private String phoneNumber;

}
