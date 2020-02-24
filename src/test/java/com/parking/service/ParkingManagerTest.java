package com.parking.service;

import com.parking.DAO.SlotDAO;
import com.parking.DAO.TicketDAO;
import com.parking.exception.CarAlreadyParkedException;
import com.parking.model.Car;
import com.parking.model.CarType;
import com.parking.model.Slot;
import com.parking.model.Ticket;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ParkingManagerTest {

    @Mock
    private SlotDAO slotDAO;

    @Mock
    private TicketDAO ticketDAO;

    private ParkingManager parkingManager;

    @Before
    public void init() {
        Slot slot = new Slot(CarType.ELECTRIC_LOW_POWER, true);
        slot.setId(1L);
        when(slotDAO.findById(1L)).thenReturn(Optional.of(slot));
        Ticket ticket = new Ticket("AAAA", slot, LocalDateTime.of(2020,2,15, 10, 30));
        when(ticketDAO.findAll()).thenReturn(Arrays.asList(ticket));

        TicketingService ticketingService = new TicketingService(ticketDAO);
        SlotService slotService = new SlotService(slotDAO);
        parkingManager = new ParkingManager(ticketingService, slotService);
    }

    @Test
    public void testCarAlreadyParkedExceptionIsThrown() {
        Car car = new Car("AAAA", CarType.ELECTRIC_LOW_POWER);
        try {
            Optional<Slot> parkingSlot = parkingManager.enterParking(car);
        }catch (Exception e) {
            assertEquals(CarAlreadyParkedException.class, e.getClass());
        }
    }

    @Test
    public void testEnterParkingSlotFound() throws CarAlreadyParkedException {
        Car car = new Car("AAAA", CarType.ELECTRIC_LOW_POWER);
        try {
            Optional<Slot> parkingSlot = parkingManager.enterParking(car);
            assertTrue(parkingSlot.isPresent());
            assertFalse(parkingSlot.get().isFree());
        }catch(Exception e){}
    }

    @Test
    public void testEnterParkingSlotNotFound() throws CarAlreadyParkedException {
        try {
            Car car = new Car("AAAA", CarType.STANDARD);
            Optional<Slot> parkingSlot = parkingManager.enterParking(car);
            assertTrue(parkingSlot.isEmpty());
        }catch(Exception e){}
    }

    @Test
    public void testExitParkingTicketFound() {
        Optional<Ticket> parkingTicket = parkingManager.exitParking("AAAA");
        assertTrue(parkingTicket.isPresent());
        assertEquals("AAAA", parkingTicket.get().getNumberPlate());
        assertNotNull(parkingTicket.get().getPrice());
        assertNotNull(parkingTicket.get().getEndTime());
    }

    @Test
    public void testExitParkingTicketNotFound() {
        Optional<Ticket> parkingTicket = parkingManager.exitParking("BBBB");
        assertTrue(parkingTicket.isEmpty());
    }
}
