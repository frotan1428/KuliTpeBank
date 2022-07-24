package com.tpebank.service;

import com.tpebank.domain.Account;
import com.tpebank.domain.User;
import com.tpebank.exception.ResourceNotFoundException;
import com.tpebank.exception.message.ExceptionMessages;
import com.tpebank.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

@Service
@AllArgsConstructor
public class AccountService {

    AccountRepository accountRepository;

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



}
