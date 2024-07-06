package com.academy.Ecommerce.controller;

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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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

}
