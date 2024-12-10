package com.epam.training.ticketservice.repository;

import com.epam.training.ticketservice.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findAccountByUsernameAndPassword(String username, String password);

    boolean existsAccountByUsername(String username);
}