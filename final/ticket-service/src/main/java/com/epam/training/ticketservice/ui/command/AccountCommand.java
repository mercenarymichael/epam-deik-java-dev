package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.dto.AccountDto;
import com.epam.training.ticketservice.entity.Role;
import com.epam.training.ticketservice.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@AllArgsConstructor
public class AccountCommand {
    private final AccountService accountService;

    @ShellMethod(key = "sign in privileged", value = "Signing in with admin privileges")
    public String loginAsPrivileged(String username, String password) {
        return accountService.login(username, password, true)
                .map(accountDto -> "Signed in as: " + accountDto.getUsername())
                .orElse("Login failed due to incorrect credentials");
    }

    @ShellMethod(key = "sign in", value = "Signing in as regular user")
    public String login(String username, String password) {
        return accountService.login(username, password, false)
                .map(accountDto -> "Signed in as: " + accountDto.getUsername())
                .orElse("Login failed due to incorrect credentials");
    }

    @ShellMethod(key = "sign out", value = "Signing out from account")
    public String logout() {
        return accountService.logout()
                .map(accountDto -> "Signed out from account: " + accountDto.getUsername())
                .orElse("You are not signed in");
    }

    @ShellMethod(key = "describe account", value = "Prints currently logged in account name")
    public String describe() {
        return accountService.describe()
                .map(accountDto -> {
                    String accountType = accountService.isPrivileged() ? "privileged account" : "account";
                    return "Signed in with " + accountType + " '" + accountDto.getUsername() + "'";
                })
                .orElse("You are not signed in");
    }

}
