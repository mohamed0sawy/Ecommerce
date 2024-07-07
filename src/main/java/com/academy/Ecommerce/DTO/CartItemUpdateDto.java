package com.academy.Ecommerce.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CartItemUpdateDto {
    private Long cartItemId;
    private int quantity;
}
