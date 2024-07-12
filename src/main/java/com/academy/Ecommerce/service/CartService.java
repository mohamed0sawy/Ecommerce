package com.academy.Ecommerce.service;

import com.academy.Ecommerce.model.Cart;
import com.academy.Ecommerce.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;

    public Optional<Cart> findCartById(Long id){
        return cartRepository.findById(id);
    }

    public Cart findCartByUserId(Long userId){
        return cartRepository.findByUserId(userId);
    }

    public Cart saveCart(Cart cart){
        return cartRepository.save(cart);
    }
}
