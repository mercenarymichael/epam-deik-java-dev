package com.epam.training.ticketservice.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Table(name = "booking")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Screening screening;

    @ManyToOne(fetch = FetchType.EAGER)
    private Account account;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<BookedSeat> bookedSeats;

    private Integer amount;

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        StringBuilder sb = new StringBuilder();

        sb.append("Seats ");
        sb.append(bookedSeats.stream()
                .map(Objects::toString)
                .collect(Collectors.joining(", ")));
        sb.append(" on " + screening.getMovie().getTitle());
        sb.append(" in room " + screening.getRoom().getName());
        sb.append(" starting at " + screening.getStartDateTime().format(formatter));
        sb.append(" for " + amount + " HUF");
        return sb.toString();
    }
}
