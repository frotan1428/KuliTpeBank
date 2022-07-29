package com.tpebank.dto;

import com.tpebank.domain.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    private LocalDateTime date;
    private String description;
    private TransactionType type;
    private double amount;
    private BigDecimal availableBalance;
}
