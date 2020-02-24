package com.parking.service;

import com.parking.DAO.TicketDAO;
import com.parking.model.Slot;
import com.parking.model.CarType;
import com.parking.model.PricingPolicy;
import com.parking.model.Ticket;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TicketingServiceTest {

    @Mock
    private TicketDAO ticketDAO;

    @InjectMocks
    private TicketingService ticketingService;

    @Test
    public void testUpdateTicketWithDefaultPricingPolicy() {
        Slot slot = new Slot(CarType.ELECTRIC_LOW_POWER, true);
        Ticket ticket = new Ticket("AAAA", slot, LocalDateTime.of(2020,2,15, 10, 30));
        ticketingService.finalizeTicket(ticket, LocalDateTime.of(2020,2,15, 12, 29));
        assertNotNull(ticket.getEndTime());
        assertNotNull(ticket.getPrice());
        assertEquals(3.0, ticket.getPrice(), 0.0);
    }

    @Test
    public void testUpdateTicketWithUpdatedPricingPolicy() {
        ticketingService.setPricingPolicy(new PricingPolicy(2.5, 3.5));
        Slot slot = new Slot(CarType.ELECTRIC_LOW_POWER, true);
        Ticket ticket = new Ticket("AAAA", slot, LocalDateTime.of(2020,2,15, 10, 30));
        ticketingService.finalizeTicket(ticket, LocalDateTime.of(2020,2,15, 12, 29));
        assertNotNull(ticket.getEndTime());
        assertNotNull(ticket.getPrice());
        assertEquals(9.5, ticket.getPrice(), 0.0);
    }
}
