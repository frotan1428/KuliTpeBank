package com.tpebank.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecipientRequest  {

    @Size(min=1, max=100,message="Your name '${validatedValue}' must be between {min} and {max} chars long")
    @NotNull(message="Please provide your name")
    private String name;

    @NotNull
    private Long accountNumber;
}
