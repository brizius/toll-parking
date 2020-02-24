package com.parking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ParkingSlotNotFoundException extends Exception {
    public ParkingSlotNotFoundException() {
        super("No parking slot found");
    }
}
