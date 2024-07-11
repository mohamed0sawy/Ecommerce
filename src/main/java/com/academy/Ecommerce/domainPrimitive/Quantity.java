package com.academy.Ecommerce.domainPrimitive;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Quantity {
    int value;

    public Quantity(int value) {
        if (value < 1){
            throw new IllegalArgumentException("Quantity cannot be less than 1");
        }
        this.value = value;
    }

    public int value(){
        return value;
    }
}
