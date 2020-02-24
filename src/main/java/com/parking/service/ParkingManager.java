package com.parking.service;

import com.parking.exception.CarAlreadyParkedException;
import com.parking.model.Car;
import com.parking.model.PricingPolicy;
import com.parking.model.Slot;
import com.parking.model.Ticket;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Handles the car enter and exit and communicates with the ticketingService
 */
@Data
@Component
public class ParkingManager {

    private TicketingService ticketingService;

    private SlotService slotService;

    /**
     * ParkingManager constructor
     *
     * @param slotService
     * @param ticketingService the service responsible for ticket related operations
     */
    @Autowired
    public ParkingManager(TicketingService ticketingService,
                          SlotService slotService) {
        this.ticketingService = ticketingService;
        this.slotService = slotService;
    }

    /**
     * Get a free parking spot and creates the ticket
     *
     * @param car
     * @return the free parking spot
     */
    public synchronized Optional<Slot> enterParking(Car car) throws CarAlreadyParkedException {
        if (ticketingService.getTicketByNumberPlate(car.getNumberPlate()).isPresent()) {
            throw new CarAlreadyParkedException();
        }
        Optional<Slot> freeParkingSlot = slotService.getFreeSlot(car.getType());
        if (freeParkingSlot.isPresent()) {
            slotService.updateSlotAvailability(freeParkingSlot.get(), false);
            ticketingService.createTicket(car.getNumberPlate(), freeParkingSlot.get());
        }
        return freeParkingSlot;
    }

    /**
     * Update the ticket with the end time and the price
     *
     * @param numberPlate
     * @return the updated ticket
     */
    public synchronized Optional<Ticket> exitParking(String numberPlate) {
        Optional<Ticket> parkingTicket = ticketingService.getTicketByNumberPlate(numberPlate);
        if (parkingTicket.isPresent()) {
            Ticket ticket = parkingTicket.get();
            Slot slotToEmpty = ticket.getSlot();
            Optional<Slot> slot = slotService.getSlotById(slotToEmpty.getId());
            slotService.updateSlotAvailability(slot.get(), true);
            ticketingService.finalizeTicket(ticket, LocalDateTime.now());
        }
        return parkingTicket;
    }

    /**
     * Update the default pricing policy
     *
     * @param pricingPolicy
     * @return
     */
    public boolean updatePricingPolicy(PricingPolicy pricingPolicy) {
        ticketingService.setPricingPolicy(pricingPolicy);
        return true;
    }
}
