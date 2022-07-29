package com.tpebank.repository;

import com.tpebank.domain.Account;
import com.tpebank.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.lang.management.OperatingSystemMXBean;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {

    Optional<Account> findByAccountNumber(Long accountNumber);

     Optional<Account> findByUser(User user);


     @Query("SELECT count(r)>0 from Recipient r inner join fetch User u on r.user.id=:userId where r.account.id=:accountId")
     Boolean existsRecipient(@Param("userId") Long id, @Param("accountId") Long accountId);


     @Query("select SUM(a.accountBalance) FROM Account a")
     Double getTotalBalance();


}
