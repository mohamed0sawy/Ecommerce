package com.academy.Ecommerce.service;

import com.academy.Ecommerce.model.CartItem;
import com.academy.Ecommerce.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemService {
    @Autowired
    private CartItemRepository cartItemRepository;

    public CartItem createCartItem(CartItem cartItem) {
        return cartItemRepository.save(cartItem);
    }

    public List<CartItem> createCartItemList(List<CartItem> cartItem) {
        return cartItemRepository.saveAll(cartItem);
    }
}
