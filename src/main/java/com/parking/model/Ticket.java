package com.parking.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    @NonNull private String numberPlate;

    @ManyToOne
    @NonNull private Slot slot;

    private double price;

    @NonNull private LocalDateTime startTime;

    private LocalDateTime endTime;

}
