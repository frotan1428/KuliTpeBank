package com.tpebank.service;

import com.tpebank.domain.Account;
import com.tpebank.domain.Transaction;
import com.tpebank.domain.User;
import com.tpebank.domain.enums.TransactionType;
import com.tpebank.dto.AccountDTO;
import com.tpebank.dto.AdminAccountDTO;
import com.tpebank.dto.AdminTransactionDTO;
import com.tpebank.dto.TransactionDTO;
import com.tpebank.dto.response.BankStatementResponse;
import com.tpebank.dto.response.CustomerStatementResponse;
import com.tpebank.exception.ResourceNotFoundException;
import com.tpebank.exception.message.ExceptionMessages;
import com.tpebank.repository.AccountRepository;
import com.tpebank.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TransactionService {

    private TransactionRepository transactionRepository;

    private AccountRepository accountRepository;

    private ModelMapper modelMapper;

    public void createTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

   //2022-07-28  07-28-2022 00:00:01
    //07-28-2022 23:59:59
    public BankStatementResponse calculateBankStatement(LocalDate startDate, LocalDate endDate){

        LocalDateTime sDate=startDate.atStartOfDay();
        LocalDateTime eDate=endDate.atTime(LocalTime.MAX);

        List<Transaction> transactionList=transactionRepository.getBankStatement(sDate,eDate);

        List<AdminTransactionDTO> transactionsDTO=transactionList.stream().
                map(this::convertToAdminTransactionDTO).collect(Collectors.toList());

        double totalBalance= accountRepository.getTotalBalance();

        BankStatementResponse response=new BankStatementResponse();
        response.setList(transactionsDTO);
        response.setTotalBalance(totalBalance);

        return response;
    }

    public CustomerStatementResponse calculateCustomerStatement(LocalDate startDate, LocalDate endDate, User user){
        LocalDateTime sDate=startDate.atStartOfDay();
        LocalDateTime eDate=endDate.atTime(LocalTime.MAX);

        Account account= accountRepository.findByUser(user).orElseThrow(()->new
                ResourceNotFoundException(String.format(ExceptionMessages.USERACCOUNT_NOT_FOUND_MESSAGE,user.getId())));

       List<Transaction> transactions= transactionRepository.getCustomerStatement(account.getId(),sDate,eDate);

       List<Transaction> depositTransactions=transactions.stream().filter(t->t.getType()==TransactionType.DEPOSIT).collect(Collectors.toList());
        List<Transaction> withdrawTransactions=transactions.stream().filter(t->t.getType()==TransactionType.WITHDRAW).collect(Collectors.toList());
        List<Transaction> transferTransactions=transactions.stream().filter(t->t.getType()==TransactionType.TRANSFER).collect(Collectors.toList());

        double sumDeposit=depositTransactions.stream().mapToDouble(t->t.getAmount()).sum();
        double sumWithdraw=withdrawTransactions.stream().mapToDouble(t->t.getAmount()).sum();
        double sumTransfer=transferTransactions.stream().mapToDouble(t->t.getAmount()).sum();

        List<TransactionDTO> transactionDTOS= transactions.stream().map(this::convertToTransactionDTO).collect(Collectors.toList());

       AccountDTO accountDTO=new AccountDTO();
       accountDTO.setAccountBalance(account.getAccountBalance());
       accountDTO.setAccountNumber(account.getAccountNumber());

       CustomerStatementResponse response=new CustomerStatementResponse();
       response.setAccount(accountDTO);
       response.setList(transactionDTOS);
       response.setTotalDeposit(sumDeposit);
       response.setTotalWithDraw(sumWithdraw);
       response.setTotalTransfer(sumTransfer);

       return response;
    }


    public TransactionDTO convertToTransactionDTO(Transaction transaction){
        return modelMapper.map(transaction,TransactionDTO.class);
    }


    private AdminTransactionDTO convertToAdminTransactionDTO(Transaction transaction){

        AdminTransactionDTO adminTransactionDTO=new AdminTransactionDTO();

        AdminAccountDTO adminAccountDTO= convertToAdminAccountDTO(transaction.getAccount());

        adminTransactionDTO.setAccount(adminAccountDTO);

        adminTransactionDTO.setAmount(transaction.getAmount());
        adminTransactionDTO.setAvailableBalance(transaction.getAvailableBalance());
        adminTransactionDTO.setDescription(transaction.getDescription());
        adminTransactionDTO.setDate(transaction.getDate());

        return adminTransactionDTO;
    }

    private AdminAccountDTO convertToAdminAccountDTO(Account account){
        AdminAccountDTO adminAccountDTO=new AdminAccountDTO();
        adminAccountDTO.setFirstName(account.getUser().getFirstName());
        adminAccountDTO.setLastName(account.getUser().getLastName());
        adminAccountDTO.setSsn(account.getUser().getSsn());
        adminAccountDTO.setEmail(account.getUser().getEmail());
        adminAccountDTO.setPhoneNumber(account.getUser().getPhoneNumber());

        adminAccountDTO.setAccountBalance(account.getAccountBalance());
        adminAccountDTO.setAccountNumber(account.getAccountNumber());
        return adminAccountDTO;
    }



}
