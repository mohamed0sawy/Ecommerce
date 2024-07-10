package com.academy.Ecommerce.service;

import com.academy.Ecommerce.model.Cart;
import com.academy.Ecommerce.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    public Cart createCart(Cart cart) {
        return cartRepository.save(cart);
    }
}
