package com.tpebank.repository;

import com.tpebank.domain.User;
import com.tpebank.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long>  {

   Boolean existsByUserName(String userName);

   Boolean existsByEmail(String email);

   Boolean existsBySsn(String ssn);
   Optional<User> findByUserNameAndEnabledTrue(String userName);

   Optional<User> findOneWithAuthoritiesByUserName(String userName);

   Optional<User>findByUserName(String userName);


   @Query("SELECT new com.tpebank.dto.UserDTO(u) from User u")
   Page<UserDTO> findUserPage(Pageable pageable);

}
