package com.tpebank.service;

import com.tpebank.domain.Account;
import com.tpebank.domain.Transaction;
import com.tpebank.domain.User;
import com.tpebank.domain.enums.TransactionType;
import com.tpebank.dto.request.TransactionRequest;
import com.tpebank.dto.request.TransferRequest;
import com.tpebank.exception.BalanceNotAvailableException;
import com.tpebank.exception.ResourceNotFoundException;
import com.tpebank.exception.message.ExceptionMessages;
import com.tpebank.repository.AccountRepository;
import com.tpebank.security.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

@Service
@AllArgsConstructor

public class AccountService {

    private AccountRepository accountRepository;

    //private UserService userService;

    private TransactionService transactionService;
    public Account createAccount(User user){
        Account account =new Account();
        Long accountNumber=getAccountNumber();
        account.setAccountNumber(accountNumber);
        account.setAccountBalance(BigDecimal.valueOf(0));
        account.setUser(user);

        accountRepository.save(account);
        return  account;
    }

    private Long getAccountNumber(){
        long smallest=1000_0000_0000_0000L;
        long biggest=9999_9999_9999_9999L;

        return ThreadLocalRandom.current().nextLong(smallest,biggest);
    }

    public Account findByAccountNumber(Long number){
       return  accountRepository.findByAccountNumber(number).orElseThrow(()->
                 new ResourceNotFoundException(String.format(ExceptionMessages.ACCOUNT_NOT_FOUND_MESSAGE,number)));
    }


   public Account getAccount(User user){
        return accountRepository.findByUser(user).orElseThrow(()->new
                ResourceNotFoundException(String.format(ExceptionMessages.USERACCOUNT_NOT_FOUND_MESSAGE,user.getId())));
   }


    @Transactional
    public void deposit(TransactionRequest transactionRequest, User user){

        Account account= getAccount(user);
        double amount =transactionRequest.getAmount();

        account.setAccountBalance(account.getAccountBalance().add(BigDecimal.valueOf(amount)));

        Transaction transaction=new Transaction();
        transaction.setDate(LocalDateTime.now());
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setAvailableBalance(account.getAccountBalance());
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setDescription(transactionRequest.getComment());

        transactionService.createTransaction(transaction);

        accountRepository.save(account);
    }


    @Transactional
    public void withdraw(TransactionRequest transactionRequest, User user){

        Account account= getAccount(user);
        double amount =transactionRequest.getAmount();

        if(account.getAccountBalance().longValue()<transactionRequest.getAmount()){
            throw new BalanceNotAvailableException(ExceptionMessages.BALANCE_NOT_AVAILABLE_MESSAGE);
        }

        account.setAccountBalance(account.getAccountBalance().subtract(BigDecimal.valueOf(amount)));

        Transaction transaction=new Transaction();
        transaction.setDate(LocalDateTime.now());
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setAvailableBalance(account.getAccountBalance());
        transaction.setType(TransactionType.WITHDRAW);
        transaction.setDescription(transactionRequest.getComment());

        transactionService.createTransaction(transaction);

        accountRepository.save(account);
    }


    @Transactional
    public void transfer(TransferRequest transferRequest,User user){
        Account accountFrom= getAccount(user);

        if(accountFrom.getAccountBalance().longValue()<transferRequest.getAmount()){
            throw new BalanceNotAvailableException(ExceptionMessages.BALANCE_NOT_AVAILABLE_MESSAGE);
        }

        //TODO:check if this recipient is in the recipient list or not

       Account accountTo= accountRepository.findByAccountNumber(transferRequest.getRecipientNumber()).
                orElseThrow(()->new ResourceNotFoundException
                        (String.format(ExceptionMessages.ACCOUNT_NOT_FOUND_MESSAGE,transferRequest.getRecipientNumber())));



        double amount=transferRequest.getAmount();

        accountFrom.setAccountBalance(accountFrom.getAccountBalance().subtract(BigDecimal.valueOf(amount)));

        accountTo.setAccountBalance(accountTo.getAccountBalance().add(BigDecimal.valueOf(amount)));


        LocalDateTime now= LocalDateTime.now();

        //sender transaction
        Transaction transactionFrom=new Transaction();
        transactionFrom.setDate(now);
        transactionFrom.setAccount(accountFrom);
        transactionFrom.setAmount(amount);
        transactionFrom.setAvailableBalance(accountFrom.getAccountBalance());
        transactionFrom.setType(TransactionType.TRANSFER);
        transactionFrom.setDescription("Transferred to "+accountTo.getAccountNumber());


        //receiver transaction
        Transaction transactionTo=new Transaction();
        transactionTo.setDate(now);
        transactionTo.setAccount(accountTo);
        transactionTo.setAmount(amount);
        transactionTo.setAvailableBalance(accountTo.getAccountBalance());
        transactionTo.setType(TransactionType.TRANSFER);
        transactionTo.setDescription("Transferred from "+accountFrom.getAccountNumber());

        transactionService.createTransaction(transactionFrom);
        transactionService.createTransaction(transactionTo);

        accountRepository.save(accountTo);
        accountRepository.save(accountFrom);
    }




}
