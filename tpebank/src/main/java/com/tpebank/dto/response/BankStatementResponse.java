package com.tpebank.dto.response;

import com.tpebank.dto.AdminTransactionDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BankStatementResponse {
    private List<AdminTransactionDTO> list;
    private double totalBalance;
}
