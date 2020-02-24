package com.parking.DAO;

import com.parking.model.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SlotDAO extends JpaRepository<Slot, Long> {}