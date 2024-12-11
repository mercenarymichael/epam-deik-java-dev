package com.epam.training.ticketservice.service.impl;

import com.epam.training.ticketservice.entity.Account;
import com.epam.training.ticketservice.entity.Booking;
import com.epam.training.ticketservice.entity.Role;
import com.epam.training.ticketservice.repository.AccountRepository;
import com.epam.training.ticketservice.service.AccountService;
import com.epam.training.ticketservice.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AccountServiceImplTest {

    private final AccountRepository accountRepository = mock(AccountRepository.class);
    private final BookingService bookingService = mock(BookingService.class);
    private Account account;
    private Account privilegedAccount;
    private Booking booking = mock(Booking.class);

    private final AccountService accountService = new AccountServiceImpl(accountRepository);

    @BeforeEach
    void setUp() {
        account = Account.builder()
                .username("miki")
                .password("asd")
                .role(Role.USER)
                .bookings(new ArrayList<>())
                .build();
        privilegedAccount = Account.builder()
                .username("admin")
                .password("admin")
                .role(Role.ADMIN)
                .build();
    }

    @Test
    void testLoginWithBadCredentials() {
        //given
        when(accountRepository.findAccountByUsernameAndPassword("alma", "asd"))
        .thenReturn(Optional.empty());
        //when
        assertFalse(accountService.login("alma", "asd").isPresent());
        assertFalse(accountService.isLoggedIn());
        //then
    }

    @Test
    void testLoginWithValidCredentials() {
        when(accountRepository.findAccountByUsernameAndPassword("miki", "asd"))
                .thenReturn(Optional.of(account));
        assertTrue(accountService.login("miki", "asd").isPresent());
        assertTrue(accountService.isLoggedIn());
        assertFalse(accountService.getIsPrivileged());
    }

    @Test
    void testPrivilegedLoginWithPrivilegedAccount() {
        //given
        accountService.setIsPrivileged(true);
        //when
        when(accountRepository.findAccountByUsernameAndPassword("admin", "admin"))
                .thenReturn(Optional.of(this.privilegedAccount));
        //then
        assertTrue(accountService.login("admin", "admin").isPresent());
        assertTrue(accountService.isLoggedIn());
        assertTrue(accountService.getIsPrivileged());
    }

    @Test
    void testPrivilegedLoginWithNotPrivilegedAccount() {
        accountService.setIsPrivileged(true);
        when(accountRepository.findAccountByUsernameAndPassword("miki", "asd"))
                .thenReturn(Optional.of(this.account));
        assertFalse(accountService.login("miki", "asd").isPresent());
        assertFalse(accountService.isLoggedIn());
        assertFalse(accountService.getIsPrivileged());
    }

    @Test
    void testLogoutWithLoggedInAccount() {
        when(accountRepository.findAccountByUsernameAndPassword("miki", "asd"))
                .thenReturn(Optional.of(this.account));

        accountService.login("miki", "asd");

        // When
        Optional<Account> result = accountService.logout();

        // Then
        assertTrue(result.isPresent());
        assertEquals(this.account, result.get());
        assertTrue(accountService.getCurrentAccount().isEmpty());
    }

    @Test
    void testLogoutWithoutLoggedInAccount() {
        when(accountRepository.findAccountByUsernameAndPassword("miki", "asd"))
                .thenReturn(Optional.empty());

        accountService.login("miki", "asd");

        // When
        Optional<Account> result = accountService.logout();

        // Then
        assertFalse(result.isPresent());
        assertTrue(accountService.getCurrentAccount().isEmpty());
    }

    @Test
    void testDescribeWhenAccountIsPrivileged() {
        when(accountRepository.findAccountByUsernameAndPassword("admin", "admin"))
                .thenReturn(Optional.of(this.privilegedAccount));
        accountService.setIsPrivileged(true);
        accountService.login("admin", "admin");
        assertEquals("Signed in with privileged account 'admin'", accountService.describe());
    }

    @Test
    void testDescribeWhenAccountIsNotPrivilegedAndBookingIsEmpty() {
        when(accountRepository.findAccountByUsernameAndPassword("miki", "asd"))
                .thenReturn(Optional.of(this.account));
        accountService.login("miki", "asd");
        assertEquals("Signed in with account 'miki'\n"
                + "You have not booked any tickets yet", accountService.describe());
    }

    @Test
    void testDescribeWhenAccountIsNotPrivilegedAndHasBooking() {
        Account bookingAccount = this.account;
        bookingAccount.setBookings(List.of(booking));
        when(accountRepository.findAccountByUsernameAndPassword("miki", "asd"))
                .thenReturn(Optional.of(bookingAccount));
        when(booking.toString())
                .thenReturn("Seats (5,5) on Sátántangó in room Pedersoli starting at 2021-03-15 10:45 for 1500 HUF");

        accountService.login("miki", "asd");

        assertEquals("Signed in with account 'miki'\n"
                + "Your previous bookings are\n"
                + booking.toString(), accountService.describe());
    }

    @Test
    void testRegisterUserWithNewAccount() {
        when(accountRepository.existsAccountByUsername("miki"))
                .thenReturn(false);

        assertTrue(accountService.registerUser("miki", "asd"));
    }

    @Test
    void testRegisterUserWithExistingAccount() {
        when(accountRepository.existsAccountByUsername("miki"))
                .thenReturn(true);

        assertFalse(accountService.registerUser("miki", "asd"));
    }
}