package com.academy.Ecommerce.service;

import com.academy.Ecommerce.exception.NotFoundException;
import com.academy.Ecommerce.model.Address;
import com.academy.Ecommerce.model.Order;
import com.academy.Ecommerce.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import com.academy.Ecommerce.repository.OrderRepository;
import com.academy.Ecommerce.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Order> getOrderHistoryByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));
        return user.getOrders();
    }

    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }

    public Order getOrderById(Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        return optionalOrder.orElse(null);
    }
}
