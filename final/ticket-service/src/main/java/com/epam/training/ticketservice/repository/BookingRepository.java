package com.epam.training.ticketservice.repository;

import com.epam.training.ticketservice.entity.Account;
import com.epam.training.ticketservice.entity.Booking;
import com.epam.training.ticketservice.entity.Screening;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findBookingByScreening(Screening screening);

    List<Booking> findAllByAccount(Optional<Account> currentAccount);

    Optional<Booking> findBookingByScreeningAndAccount(Screening screening, Optional<Account> currentAccount);
}