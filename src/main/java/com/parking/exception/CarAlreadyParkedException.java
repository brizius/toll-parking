package com.parking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class CarAlreadyParkedException extends Exception {
    public CarAlreadyParkedException() {
        super("The car is already parked");
    }
}
