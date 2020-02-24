package com.parking.service;

import com.parking.DAO.SlotDAO;
import com.parking.model.CarType;
import com.parking.model.Slot;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Data
@Component
public class SlotService {

    private SlotDAO slotDAO;

    @Autowired
    public SlotService(SlotDAO slotDAO){
        this.slotDAO = slotDAO;
    }

    /**
     * Get an available slot for the car type
     *
     * @param type
     * @return the free slot
     */
    public Optional<Slot> getFreeSlot(CarType type) {
        return slotDAO.findAll().stream()
                .filter(p -> p.getCarType().equals(type) && p.isFree())
                .findFirst();
    }

    /**
     * Update the availability of the slot and save it in the repository
     *
     * @param slot
     * @param isFree
     */
    public void updateSlotAvailability(Slot slot, boolean isFree) {
        slot.setFree(isFree);
        slotDAO.save(slot);
    }

    /**
     * Get the slot, given the id
     *
     * @param slotId of the
     * @return
     */
    public Optional<Slot> getSlotById(long slotId) {
        return slotDAO.findById(slotId);
    }
}
