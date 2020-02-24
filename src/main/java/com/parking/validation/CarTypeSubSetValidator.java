package com.parking.validation;

import com.parking.model.CarType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class CarTypeSubSetValidator implements ConstraintValidator<CarTypeSubset, CarType> {
    private CarType[] subset;

    @Override
    public void initialize(CarTypeSubset constraint) {
        this.subset = constraint.anyOf();
    }

    @Override
    public boolean isValid(CarType value, ConstraintValidatorContext context) {
        return value == null || Arrays.asList(subset).contains(value);
    }
}
