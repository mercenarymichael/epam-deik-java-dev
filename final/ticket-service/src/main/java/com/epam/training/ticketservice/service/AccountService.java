package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.entity.Account;

import java.util.Optional;

public interface AccountService {
    Optional<Account> login(String username, String password);

    Optional<Account> logout();

    Optional<Account> getCurrentAccount();

    String describe();

    boolean registerUser(String username, String password);

    boolean getIsPrivileged();

    void setIsPrivileged(boolean isPrivileged);

    boolean isLoggedIn();
}
