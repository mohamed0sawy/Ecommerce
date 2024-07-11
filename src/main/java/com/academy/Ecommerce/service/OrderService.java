package com.academy.Ecommerce.service;

import com.academy.Ecommerce.exception.NotFoundException;
import com.academy.Ecommerce.model.*;
import com.academy.Ecommerce.repository.OrderItemRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import com.academy.Ecommerce.repository.OrderRepository;
import com.academy.Ecommerce.repository.UserRepository;
import org.springframework.security.config.annotation.web.OAuth2ResourceServerDsl;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;


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

    public void getCartItems(List<Order> orders, HttpServletRequest request){
        HttpSession session = request.getSession();

        User user = new User();
        Address address = new Address();

        List<CartItem> cartItemList = (List<CartItem>) session.getAttribute("checkedCartItems");
        Long selectedAddressId = (Long) session.getAttribute("selectedAddressId");
        Long selectedUserId = (Long) session.getAttribute("userId");
        user.setId(selectedUserId);
        address.setUser(user);
        address.setId(selectedAddressId);

        for(Order order : orders) {
            List<OrderItem> orderItems = new ArrayList<>();
            for (CartItem cartItem : cartItemList) {
                OrderItem orderItem = new OrderItem();
                orderItem.setProduct(cartItem.getProduct());
                orderItem.setQuantity(cartItem.getQuantity().getValue());

                order.setUser(user);
                order.setAddress(address);
                order.setOrderDate(LocalDateTime.now());
                orderRepository.save(order);

                orderItem.setOrder(order);
                orderItems.add(orderItem);
                orderItemRepository.saveAll(orderItems);
            }
        }
    }
}
