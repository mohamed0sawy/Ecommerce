package com.academy.Ecommerce.controller;

import com.academy.Ecommerce.model.Address;
import com.academy.Ecommerce.model.Order;
import com.academy.Ecommerce.model.OrderItem;
import com.academy.Ecommerce.model.User;
import com.academy.Ecommerce.model.CartItem;
import com.academy.Ecommerce.repository.OrderItemRepository;
import com.academy.Ecommerce.repository.OrderRepository;
import com.academy.Ecommerce.service.AddressService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import com.academy.Ecommerce.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/api/v1/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping
    public String getAllOrders(Model model, HttpServletRequest request) {
        List<Order> allOrders = orderService.getAllOrders();
        model.addAttribute("allOrders", allOrders);
        return "orders-List";
    }


    @GetMapping("/history/{userId}")
    public String getOrderHistory(@PathVariable Long userId, HttpServletRequest request, Model model,
                                  @RequestParam("payment") String paymentMethod) {
        List<Order> orderHistory = orderService.getOrderHistoryByUserId(userId);

        HttpSession session = request.getSession();
        Long selectedAddressId = (Long) session.getAttribute("selectedAddressId");
        Address selectedAddress = addressService.getAddressById(selectedAddressId);

        model.addAttribute("orders", orderHistory);
        model.addAttribute("selectedAddress", selectedAddress);
        model.addAttribute("paymentMethod", paymentMethod);
        return "order-history";
    }


    @GetMapping("/details/{orderId}")
    public String getOrderDetails(@PathVariable Long orderId, HttpServletRequest request, Model model) {
        Order order = orderService.getOrderById(orderId);

        model.addAttribute("order", order);
        return "order-details";
    }

    @GetMapping("/cartItems")
    public String getCartItems(HttpServletRequest request) {
        HttpSession session = request.getSession();

        List<CartItem> cartItemList = (List<CartItem>) session.getAttribute("checkedCartItems");
        Long userId = (Long) session.getAttribute("userId");
        Long selectedAddressId = (Long) session.getAttribute("selectedAddressId");

        User user = new User();
        user.setId(userId);

        Address address = addressService.getAddressById(selectedAddressId);
        address.setUser(user);

        Order order = new Order();
        order.setUser(user);
        order.setAddress(address);
        order.setOrderDate(LocalDateTime.now());
        orderRepository.save(order);

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItemList) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity().getValue());
            orderItems.add(orderItem);
            orderItemRepository.saveAll(orderItems);
        }
        return "success";
    }
}