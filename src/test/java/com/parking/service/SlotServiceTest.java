package com.parking.service;

import com.parking.DAO.SlotDAO;
import com.parking.model.CarType;
import com.parking.model.Slot;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SlotServiceTest {
    @Mock
    private SlotDAO slotDAO;

    @InjectMocks
    private SlotService slotService;

    @Test
    public void getFreeSlotTestFreeSameType() {
        Slot freeSlot = new Slot(CarType.ELECTRIC_LOW_POWER, true);
        when(slotDAO.findAll()).thenReturn(Arrays.asList(freeSlot));
        Optional<Slot> slot = slotService.getFreeSlot(CarType.ELECTRIC_LOW_POWER);
        assertTrue(slot.isPresent());
    }

    @Test
    public void getFreeSlotTestOccupiedSameType() {
        Slot freeSlot = new Slot(CarType.ELECTRIC_LOW_POWER, false);
        when(slotDAO.findAll()).thenReturn(Arrays.asList(freeSlot));
        Optional<Slot> slot = slotService.getFreeSlot(CarType.ELECTRIC_LOW_POWER);
        assertFalse(slot.isPresent());
    }

    @Test
    public void getFreeSlotTestFreeDifferentType() {
        Slot freeSlot = new Slot(CarType.STANDARD, true);
        when(slotDAO.findAll()).thenReturn(Arrays.asList(freeSlot));
        Optional<Slot> slot = slotService.getFreeSlot(CarType.ELECTRIC_LOW_POWER);
        assertFalse(slot.isPresent());
    }
}
