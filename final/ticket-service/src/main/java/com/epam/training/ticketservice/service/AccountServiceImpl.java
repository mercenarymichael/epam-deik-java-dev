package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.dto.AccountDto;
import com.epam.training.ticketservice.entity.Account;
import com.epam.training.ticketservice.entity.Role;
import com.epam.training.ticketservice.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private AccountDto loggedInAccount = null;
    private boolean isPrivileged = false;

    @Override
    public Optional<AccountDto> login(String username, String password, boolean requirePrivileged) {
        Optional<Account> account = accountRepository.findAccountByUsernameAndPassword(username, password);
        if (account.isEmpty()) {
            return Optional.empty();
        }
        if (requirePrivileged && !account.get().getRole().equals(Role.ADMIN)) {
            return Optional.empty();
        } else if (requirePrivileged) {
            isPrivileged = true;
        }

        loggedInAccount = AccountDto.builder()
                .username(account.get().getUsername())
                .password(account.get().getPassword())
                .role(account.get().getRole())
                .build();
        return describe();
    }


    @Override
    public Optional<AccountDto> logout() {
        Optional<AccountDto> previouslyLoggedInAccount = describe();
        loggedInAccount = null;
        isPrivileged = false;
        return previouslyLoggedInAccount;
    }

    @Override
    public Optional<AccountDto> describe() {
        return Optional.ofNullable(loggedInAccount);
    }

    @Override
    public void registerUser(String username, String password) {
        Account account = Account.builder()
                .username(username)
                .password(password)
                .role(Role.USER)
                .build();
        accountRepository.save(account);
    }

    @Override
    public boolean isPrivileged() {
        return isPrivileged;
    }
}
