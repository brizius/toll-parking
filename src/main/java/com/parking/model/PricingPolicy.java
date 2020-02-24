package com.parking.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
@AllArgsConstructor
public class PricingPolicy {

    private static final double DEFAULT_FIXED_AMOUNT = 0.0;

    private static final double DEFAULT_HOUR_PRICE = 1.5;

    @NotNull
    @PositiveOrZero
    private double fixedAmount;

    @NotNull
    @DecimalMin("0.01")
    private double hourPrice;

    public PricingPolicy() {
        this.fixedAmount = DEFAULT_FIXED_AMOUNT;
        this.hourPrice = DEFAULT_HOUR_PRICE;
    }
}
