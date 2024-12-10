package com.epam.training.ticketservice.dto;

import com.epam.training.ticketservice.entity.Role;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
@Builder
public class AccountDto {
    private final String username;
    private final String password;
    private final Role role;

    public AccountDto(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AccountDto entity = (AccountDto) o;
        return Objects.equals(this.username, entity.username)
                && Objects.equals(this.password, entity.password)
                && Objects.equals(this.role, entity.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, role);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "("
                + "username = " + username + ", "
                + "password = " + password + ", "
                + "role = " + role + ")";
    }
}