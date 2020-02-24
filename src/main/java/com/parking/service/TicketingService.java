package com.parking.service;

import com.parking.DAO.TicketDAO;
import com.parking.model.Slot;
import com.parking.model.PricingPolicy;
import com.parking.model.Ticket;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Data
@Component
public class TicketingService {

    private TicketDAO ticketDAO;

    private PricingPolicy pricingPolicy;

    /**
     * Sets the DAO and a default pricing policy
     *
     * @param ticketDAO
     */
    @Autowired
    public TicketingService(TicketDAO ticketDAO) {
        this.ticketDAO = ticketDAO;
        this.pricingPolicy = new PricingPolicy();
    }

    /**
     * Creates a new ticket and save it in the the repository
     *
     * @param numberPlate
     * @param slot the parking slot assigned to the car while entering in the parking
     */
    public void createTicket(String numberPlate, Slot slot) {
        ticketDAO.save(new Ticket(numberPlate, slot, LocalDateTime.now()));
    }

    /**
     * Get the ticket from the DAO using the numberPlate
     *
     * @param numberPlate
     * @return the ticket associated to the numberPlate
     */
    public Optional<Ticket> getTicketByNumberPlate(String numberPlate) {
        return ticketDAO.findAll().stream()
                .filter(t -> t.getNumberPlate().equals(numberPlate) && t.getEndTime() == null)
                .findFirst();
    }

    /**
     * Update the ticket with the price and endTime
     *
     * @param ticket
     * @param endTime time the car left the parking
     */
    public void finalizeTicket(Ticket ticket, LocalDateTime endTime) {
        ticket.setEndTime(endTime);
        ticket.setPrice(computePrice(ticket.getStartTime(), ticket.getEndTime()));
        ticketDAO.save(ticket);
    }

    /**
     * Compute the price of the ticket, based on the pricing policy and the duration.
     * The duration is rounded to ceiling. i.e. 2h59m is rounded to 3h
     *
     * @param startTime time when the car entered the parking
     * @param endTime   time when the car left the parking
     * @return the price of the ticket
     */
    private double computePrice(LocalDateTime startTime, LocalDateTime endTime) {
        Duration duration = Duration.between(startTime, endTime);
        long roundFloor =  duration.truncatedTo(ChronoUnit.HOURS).toHours();
        long roundCeiling =  roundFloor + 1;
        long roundedDuration = roundFloor == roundCeiling ? roundFloor : roundCeiling;
        return pricingPolicy.getFixedAmount() + pricingPolicy.getHourPrice() * roundedDuration;
    }
}
