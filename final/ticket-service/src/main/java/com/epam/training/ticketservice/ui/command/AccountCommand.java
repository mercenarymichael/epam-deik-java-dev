package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

@ShellComponent
@AllArgsConstructor
public class AccountCommand {
    private final AccountService accountService;

    @ShellMethod(key = "sign up", value = "Register new user")
    public String signUp(String username, String password) {
        return accountService.registerUser(username, password) ? "Success!" :
                "There is an existing account with this username!";
    }


    @ShellMethod(key = "sign in privileged", value = "Signing in with admin privileges")
    public String loginAsPrivileged(String username, String password) {
        accountService.setIsPrivileged(true);
        return accountService.login(username, password)
                .map(accountDto -> "Signed in as: " + accountDto.getUsername())
                .orElse("Login failed due to incorrect credentials");
    }

    @ShellMethod(key = "sign in", value = "Signing in as regular user")
    public String login(String username, String password) {
        return accountService.login(username, password)
                .map(accountDto -> "Signed in as: " + accountDto.getUsername())
                .orElse("Login failed due to incorrect credentials");
    }

    @ShellMethod(key = "sign out", value = "Signing out from account")
    @ShellMethodAvailability("isLoggedIn")
    public String logout() {
        if (!accountService.isLoggedIn()) {
            return "You are not signed in";
        }
        return accountService.logout()
                .map(accountDto -> "Signed out from account: " + accountDto.getUsername())
                .orElse("You are not signed in");
    }

    @ShellMethod(key = "describe account", value = "Prints currently logged in account name")
    public String describe() {
        if (!accountService.isLoggedIn()) {
            return "You are not signed in";
        }
        return accountService.describe();
    }


    private Availability isLoggedIn() {
        return true ? Availability.available()
                :
                Availability.unavailable("You are not signed in");
    }
}
