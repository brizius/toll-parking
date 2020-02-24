package com.parking.controller;

import com.parking.exception.CarAlreadyParkedException;
import com.parking.exception.ParkingSlotNotFoundException;
import com.parking.exception.TicketNotFoundException;
import com.parking.model.Car;
import com.parking.model.PricingPolicy;
import com.parking.model.Slot;
import com.parking.model.Ticket;
import com.parking.service.ParkingManager;
import com.parking.service.TicketingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Size;

@RestController("/api")
@Validated
public class ParkingController {

    @Autowired
    private ParkingManager parkingManager;

    @Autowired
    private TicketingService ticketingService;

    @PostMapping("/car")
    public ResponseEntity<Slot> enterParking(@Valid @RequestBody Car car) throws ParkingSlotNotFoundException,
            CarAlreadyParkedException {
        Slot slot =
                parkingManager.enterParking(car).orElseThrow(() -> new ParkingSlotNotFoundException());
        return ResponseEntity.ok(slot);
    }

    @DeleteMapping("/car/{plateNumber}")
    public ResponseEntity<Ticket> exitParking(@PathVariable("plateNumber") @Size(min = 3, max = 7) String plateNumber) throws TicketNotFoundException {
        Ticket ticket = parkingManager.exitParking(plateNumber).orElseThrow(() -> new TicketNotFoundException());
        return ResponseEntity.ok(ticket);
    }

    @PutMapping("/pricingPolicy")
    public ResponseEntity<PricingPolicy> updatePricingPolicy(@Valid @RequestBody PricingPolicy pricingPolicy) {
        parkingManager.updatePricingPolicy(pricingPolicy);
        return ResponseEntity.ok(pricingPolicy);
    }
}