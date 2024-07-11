package com.academy.Ecommerce.converter;

import com.academy.Ecommerce.domainPrimitive.Quantity;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class QuantityConverter implements AttributeConverter<Quantity, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Quantity quantity) {
        return quantity.value();
    }

    @Override
    public Quantity convertToEntityAttribute(Integer value) {
        return new Quantity(value);
    }
}
