package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.dto.AccountDto;

import java.util.Optional;

public interface AccountService {
    Optional<AccountDto> login(String username, String password, boolean requirePrivileged);

    Optional<AccountDto> logout();

    Optional<AccountDto> describe();

    void registerUser(String username, String password);

    boolean isPrivileged();
}
