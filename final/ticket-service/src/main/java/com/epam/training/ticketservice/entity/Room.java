package com.epam.training.ticketservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Room room = (Room) o;
        return id.equals(room.id)
                && name.equals(room.name)
                && seatRowCount.equals(room.seatRowCount)
                && seatColumnCount.equals(room.seatColumnCount);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + seatRowCount.hashCode();
        result = 31 * result + seatColumnCount.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Room "
                + name
                + " with "
                + (seatRowCount * seatColumnCount)
                + " seats, " + seatRowCount
                + " rows and " + seatColumnCount + " columns";
    }
}