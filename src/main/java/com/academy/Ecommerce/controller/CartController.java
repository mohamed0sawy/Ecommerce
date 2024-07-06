package com.academy.Ecommerce.controller;

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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final CartItemService cartItemService;
    private final ProductService productService;

    @GetMapping
    public String cart(){
        return "cart";
    }

    @GetMapping("/add")
    public String addToCart(@RequestParam("product_id") Long productId, HttpServletRequest request){
        HttpSession session = request.getSession();
        User existingUser = (User) session.getAttribute("user");
        Cart cart = cartService.findCartByUserId(existingUser.getId());
        Product product = productService.findProductById(productId).get();
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(new Quantity(1));
        cartItemService.saveCartItem(cartItem);
        return "redirect:/api/v1/main";
    }

}
