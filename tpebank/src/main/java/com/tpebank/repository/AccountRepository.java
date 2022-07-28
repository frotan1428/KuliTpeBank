package com.tpebank.repository;

import com.tpebank.domain.Account;
import com.tpebank.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.lang.management.OperatingSystemMXBean;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {

    Optional<Account> findByAccountNumber(Long accountNumber);

     Optional<Account> findByUser(User user);
}
