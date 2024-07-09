package com.academy.Ecommerce.controller;

import com.academy.Ecommerce.DTO.CartItemUpdateDto;
import com.academy.Ecommerce.domainPrimitive.Quantity;
import com.academy.Ecommerce.model.Cart;
import com.academy.Ecommerce.model.CartItem;
import com.academy.Ecommerce.model.Product;
import com.academy.Ecommerce.model.User;
import com.academy.Ecommerce.service.CartItemService;
import com.academy.Ecommerce.service.CartService;
import com.academy.Ecommerce.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final CartItemService cartItemService;
    private final ProductService productService;

    @GetMapping
    public String cart(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        User existingUser = (User) session.getAttribute("user");
        if (existingUser == null) {
            return "redirect:/api/v1/login";
        }
        Cart cart = cartService.findCartByUserId(existingUser.getId());
        List<CartItem> cartItems = cartItemService.findCartItemByCartId(cart.getId());
        double totalPrice = 0;
        for (CartItem cartItem : cartItems) {
            totalPrice += cartItem.getQuantity().value() * cartItem.getProduct().getPrice();
        }
        double totalPriceWithShopping = totalPrice + 30.0;
        String formattedTotalPrice = String.format("%.2f", totalPrice);
        String formattedTotalPriceWithShopping = String.format("%.2f", totalPriceWithShopping);
        model.addAttribute("totalPrice", formattedTotalPrice);
        model.addAttribute("totalPriceWithShopping", formattedTotalPriceWithShopping);
        model.addAttribute("cartItems", cartItems);
        return "cart";
    }

    @GetMapping("/add")
    public String addToCart(@RequestParam("product_id") Long productId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User existingUser = (User) session.getAttribute("user");
        if (existingUser == null) {
            return "redirect:/api/v1/login";
        }
        Cart cart = cartService.findCartByUserId(existingUser.getId());
        Product product = productService.findProductById(productId).get();
        CartItem cartItem = cartItemService.findCartItemByCartIdAndProductId(cart.getId(), product.getId());
        if (cartItem == null) {
            cartItemService.addCartItem(cart, product, 1);
        } else {
            cartItemService.incrementQuantityByOne(cart, product);
        }
        return "redirect:/api/v1/main";
    }

    @GetMapping("/remove")
    public String removeItemFromCart(@RequestParam("itemId") Long cartItemId){
        cartItemService.deleteCartItemById(cartItemId);
        return "redirect:/api/v1/cart";
    }

    @PostMapping("/checkout")
    public ResponseEntity<Map<String, Object>> checkout(@RequestBody List<CartItemUpdateDto> cartItemUpdates,
                                                        HttpServletRequest request) {
        List<CartItem> checkedCartItems = new ArrayList<>();
        for (CartItemUpdateDto cartItemUpdate : cartItemUpdates) {
            CartItem cartItem = cartItemService.findCartItemByCartItemId(cartItemUpdate.getCartItemId()).get();
            Product product = productService.findProductById(cartItem.getProduct().getId()).get();

            if (product.getStock() < cartItemUpdate.getQuantity()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Insufficient stock for product: " + product.getName() +
                        ", only " + product.getStock() + " items in stock at the moment.");
                return ResponseEntity.ok(response);
            }
        }

        for (CartItemUpdateDto cartItemUpdate : cartItemUpdates) {
            CartItem cartItem = cartItemService.findCartItemByCartItemId(cartItemUpdate.getCartItemId()).get();
            cartItem.setQuantity(new Quantity(cartItemUpdate.getQuantity()));
            checkedCartItems.add(cartItem);
            cartItemService.saveCartItem(cartItem);
        }
        double totalPrice = checkedCartItems.stream()
                .mapToDouble(item -> item.getQuantity().value() * item.getProduct().getPrice())
                .sum();
        HttpSession session = request.getSession();
        session.setAttribute("checkedCartItems", checkedCartItems);
        session.setAttribute("totalPrice", totalPrice);
        User existingUser = (User) session.getAttribute("user");
        Long userId = existingUser.getId();
        String redirectUrl = "/api/v1/Address/list?user_id=" + userId;

        return ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION, redirectUrl).build();
    }

}
