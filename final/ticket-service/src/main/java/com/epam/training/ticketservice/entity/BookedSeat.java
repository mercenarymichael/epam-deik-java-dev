package com.epam.training.ticketservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Column;


@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "booked_seat")
public class BookedSeat {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "booking_id", referencedColumnName = "id", nullable = false)
    private Booking booking;

    @Column(nullable = false)
    private Integer seatRow;

    @Column(nullable = false)
    private Integer seatCol;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BookedSeat that = (BookedSeat) o;
        return id.equals(that.id)
                && booking.equals(that.booking)
                && seatRow.equals(that.seatRow)
                && seatCol.equals(that.seatCol);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + booking.hashCode();
        result = 31 * result + seatRow.hashCode();
        result = 31 * result + seatCol.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "(" + seatRow + "," + seatCol + ")";
    }
}