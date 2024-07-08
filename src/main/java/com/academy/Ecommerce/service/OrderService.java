package com.academy.Ecommerce.service;

import com.academy.Ecommerce.exception.NotFoundException;
import com.academy.Ecommerce.model.Address;
import com.academy.Ecommerce.model.Order;
import com.academy.Ecommerce.model.OrderItem;
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

    public List<Order> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        for (Order order : orders) {
            double totalPrice = calculateTotalPrice(order.getItems());
            order.setTotalPrice(totalPrice);
        }
        return orders;
    }

    public List<Order> getOrderHistoryByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));
        List<Order> orderHistory = user.getOrders();
        for (Order order : orderHistory) {
            double totalPrice = calculateTotalPrice(order.getItems());
            order.setTotalPrice(totalPrice);
        }
        return orderHistory;
    }

    public Order getOrderById(Long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            double totalPrice = calculateTotalPrice(order.getItems());
            order.setTotalPrice(totalPrice);
            return order;
        }
        return null;
    }

    private double calculateTotalPrice(List<OrderItem> items) {
        double totalPrice = 0.0;
        for (OrderItem item : items) {
            totalPrice += item.getQuantity() * item.getProduct().getPrice();
        }
        return totalPrice;
    }
}