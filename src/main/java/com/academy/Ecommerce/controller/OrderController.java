package com.academy.Ecommerce.controller;

import com.academy.Ecommerce.model.Address;
import com.academy.Ecommerce.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import com.academy.Ecommerce.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping
    public String getAllOrders(Model model){
        List<Order> AllOrders = orderService.getAllOrders();
        model.addAttribute("allOrders", AllOrders);
        return "orders-List";
    }

    @GetMapping("/history/{userId}")
    public String getOrderHistory(@PathVariable Long userId, Model model) {
        List<Order> orderHistory = orderService.getOrderHistoryByUserId(userId);
        model.addAttribute("orders", orderHistory);
        return "order-history";
    }

    @GetMapping("/details/{orderId}")
    public String getOrderDetails(@PathVariable Long orderId, Model model) {
        Order order = orderService.getOrderById(orderId);
        model.addAttribute("order", order);
        return "order-details";
    }
}