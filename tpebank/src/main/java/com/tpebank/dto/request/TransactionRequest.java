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
public class TransactionRequest {
    @NotNull
    private Double amount;

    @NotNull
    @Size(min=1, max=100, message="Please provide a comment")
    private String comment;
}
