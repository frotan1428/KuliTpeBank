package com.tpebank.repository;

import com.tpebank.domain.Account;
import com.tpebank.domain.Recipient;
import com.tpebank.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipientRepository extends JpaRepository<Recipient,Long> {

    Boolean existsByUserAndAccount(User user, Account account);


}
