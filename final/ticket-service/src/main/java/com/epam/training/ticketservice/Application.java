package com.epam.training.ticketservice;

import com.epam.training.ticketservice.entity.Account;
import com.epam.training.ticketservice.entity.Movie;
import com.epam.training.ticketservice.entity.Role;
import com.epam.training.ticketservice.repository.AccountRepository;
import com.epam.training.ticketservice.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class Application {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    private MovieRepository movieRepository;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    public void loadAdminAccount() {
        accountRepository.save(Account.builder()
                .username("admin")
                .password("admin")
                .role(Role.ADMIN)
                .build());
        System.out.println("Admin account initialized.");
        movieRepository.save(Movie.builder()
                .title("Alma")
                .genre("krimi")
                .runtime(150)
                .build());
        movieRepository.save(Movie.builder()
                .title("Inception")
                .genre("action")
                .runtime(200)
                .build());
        System.out.println("Movies initialized.");
    }
}
