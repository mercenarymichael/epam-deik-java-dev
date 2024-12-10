package com.epam.training.ticketservice.service.impl;


import com.epam.training.ticketservice.entity.Account;
import com.epam.training.ticketservice.entity.Booking;
import com.epam.training.ticketservice.entity.Role;
import com.epam.training.ticketservice.repository.AccountRepository;
import com.epam.training.ticketservice.repository.BookingRepository;
import com.epam.training.ticketservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private Account loggedInAccount = null;
    private boolean isPrivileged = false;

    @Override
    public Optional<Account> login(String username, String password) {
        Optional<Account> account = accountRepository.findAccountByUsernameAndPassword(username, password);
        if (account.isEmpty()) {
            return Optional.empty();
        }

        if (isPrivileged && !account.get().getRole().equals(Role.ADMIN)) {
            isPrivileged = false;
            return Optional.empty();
        }

        loggedInAccount = account.get();
        return getCurrentAccount();
    }


    @Override
    public Optional<Account> logout() {
        Optional<Account> previouslyLoggedInAccount = getCurrentAccount();
        loggedInAccount = null;
        isPrivileged = false;
        return previouslyLoggedInAccount;
    }


    @Override
    public Optional<Account> getCurrentAccount() {
        return Optional.ofNullable(loggedInAccount);
    }


    @Override
    @Transactional
    public String describe() {
        if (isPrivileged) {
            return "Signed in with privileged account '" + loggedInAccount.getUsername() + "'";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Signed in with account '").append(loggedInAccount.getUsername()).append("'\n");

        if (loggedInAccount.getBookings().isEmpty()) {
            sb.append("You have not booked any tickets yet");
        } else {
            sb.append("Your previous bookings are\n");
            for (Booking booking : loggedInAccount.getBookings()) {
                sb.append(booking.toString() + "\n");
            }
        }
        return sb.toString();
    }


    @Override
    public boolean registerUser(String username, String password) {
        if (accountRepository.existsAccountByUsername(username)) {
            return false;
        }
        Account account = Account.builder()
                .username(username)
                .password(password)
                .role(Role.USER)
                .build();
        accountRepository.save(account);
        return true;
    }


    @Override
    public boolean getIsPrivileged() {
        return isPrivileged;
    }


    @Override
    public void setIsPrivileged(boolean isPrivileged) {
        this.isPrivileged = isPrivileged;
    }

    @Override
    public boolean isLoggedIn() {
        return loggedInAccount != null;
    }

    @Override
    public String getUsername() {
        return loggedInAccount.getUsername();
    }
}
