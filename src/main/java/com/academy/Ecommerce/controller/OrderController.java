package com.academy.Ecommerce.controller;

import com.academy.Ecommerce.model.*;
import com.academy.Ecommerce.repository.OrderItemRepository;
import com.academy.Ecommerce.repository.OrderRepository;
import com.academy.Ecommerce.repository.ProductRepository;
import com.academy.Ecommerce.service.AddressService;
import com.academy.Ecommerce.service.CartItemService;
import com.academy.Ecommerce.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Autowired
    private CartService cartService;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private ProductRepository productRepository;



    @GetMapping
    public String getAllOrders(Model model, HttpServletRequest request) {
        List<Order> allOrders = orderService.getAllOrders();
        model.addAttribute("allOrders", allOrders);
        return "orders-List";
    }


    @GetMapping("/history/{userId}")
    public String getOrderHistory(@PathVariable Long userId, HttpServletRequest request, Model model) {

        List<Order> orderHistory = orderService.getOrderHistoryByUserId(userId);

        HttpSession session = request.getSession();

        Long selectedAddressId = (Long) session.getAttribute("selectedAddressId");



        model.addAttribute("orders", orderHistory);
        //model.addAttribute("paymentMethod", paymentMethod);
        return "order-history";
    }


    @GetMapping("/details/{orderId}")
    public String getOrderDetails(@PathVariable Long orderId, HttpServletRequest request, Model model) {

        Order order = orderService.getOrderById(orderId);
        HttpSession session = request.getSession();
        session.setAttribute("orderId", orderId);
//        Long selectedAddressId = (Long) session.getAttribute("selectedAddressId");
//        logger.debug("ORDER CONTROLLER: SELECTED ADDRESS",selectedAddressId);
//        Address selectedAddress = addressService.getAddressById(selectedAddressId);

        model.addAttribute("order", order);
//        model.addAttribute("selectedAddress", selectedAddress);
        return "order-details";
    }

    @Transactional
    @GetMapping("/cartItems")
    public String getCartItems(HttpServletRequest request, @RequestParam("payment") String paymentMethod) {
        HttpSession session = request.getSession();
        List<CartItem> cartItemList = (List<CartItem>) session.getAttribute("checkedCartItems");

        User user = (User) session.getAttribute("user");
        Long userId = user.getId();
        user.setId(userId);

        Long selectedAddressId = (Long) session.getAttribute("selectedAddressId");
        Address address = addressService.getAddressById(selectedAddressId);
        address.setUser(user);

        Order order = new Order();
        order.setUser(user);
        order.setAddress(address);
        order.setOrderDate(LocalDateTime.now());
        order.setPaymentMethod(paymentMethod);
        orderRepository.save(order);

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItemList) {
            Product product = cartItem.getProduct();
            int orderedQuantity = cartItem.getQuantity().getValue();
            int newStock = product.getStock() - orderedQuantity;
            product.setStock(newStock);
            productRepository.save(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(orderedQuantity);
            orderItems.add(orderItem);
        }
        orderItemRepository.saveAll(orderItems);

        Cart cart = cartService.findCartByUserId(userId);
        Long cartId = cart.getId();
        cartItemService.deleteCartItemsByCartId(cartId);
        session.removeAttribute("checkedCartItems");

        return "redirect:/api/v1/orders/details/" + order.getId();
    }
}