package com.parking.model;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class Car {
    @NonNull
    @NotNull
    @Size(min = 3, max = 7)
    private String numberPlate;

    @NonNull
    @NotNull
    private CarType type;
}

