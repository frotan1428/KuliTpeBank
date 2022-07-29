package com.tpebank.dto.response;

import com.tpebank.dto.AccountDTO;
import com.tpebank.dto.TransactionDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerStatementResponse {
    List<TransactionDTO> list;
    double totalDeposit;
    double totalWithDraw;
    double totalTransfer;

    AccountDTO account;

}
