package com.epam.training.ticketservice.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer seatRowCount;

    @Column(nullable = false)
    private Integer seatColumnCount;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<Screening> screenings;

}