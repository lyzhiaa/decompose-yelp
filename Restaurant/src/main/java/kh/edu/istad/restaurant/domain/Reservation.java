package kh.edu.istad.restaurant.domain;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime reservationDateTime;

    private Integer partySize;

    private String specialRequest;

    private String reservationStatus;   // (e.g., "Pending", "Confirmed", "Cancelled")
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private LocalDateTime reservationDateTime;
//
//    private Integer numberOfPeople;
//
//    private String customerName;
//
//    private String customerEmail;
//
//    private String customerPhone;
//
//    private String specialRequest;
//
//    private String reservationStatus;

}
