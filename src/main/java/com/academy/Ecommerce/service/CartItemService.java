package com.academy.Ecommerce.service;

import com.academy.Ecommerce.model.CartItem;
import com.academy.Ecommerce.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartItemService {
    private final CartItemRepository cartItemRepository;

    public List<CartItem> findCartItemByCartId(Long cartId){
        return cartItemRepository.findByCartId(cartId);
    }

    public CartItem saveCartItem(CartItem cartItem){
        return cartItemRepository.save(cartItem);
    }
}
