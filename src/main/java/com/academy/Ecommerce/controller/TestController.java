package com.academy.Ecommerce.controller;

import com.academy.Ecommerce.domainPrimitive.Quantity;
import com.academy.Ecommerce.model.*;
import com.academy.Ecommerce.repository.UserRepository;
import com.academy.Ecommerce.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.ArrayList;

@Controller
@RequestMapping("/api/orders")
public class TestController {
    @Autowired
    private UserService userService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private CartService cartService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/test")
    public String getCartItems(HttpSession session, Model model){

        //user data
        User testUser = new User();
        testUser.setUsername("testUser");
        testUser.setPassword("password");
        testUser = userService.createUser(testUser);

        User testUser2 = new User();
        testUser2.setUsername("testUser2");
        testUser2.setPassword("password");
        testUser2 = userService.createUser(testUser2);

        List<CartItem> cartItemList = new ArrayList<>();
        List<CartItem> cartItemList2 = new ArrayList<>();

        //category data
        Category category1 = new Category();
        category1.setName("Test Category 1");
        categoryService.save(category1);

        Category category2 = new Category();
        category2.setName("Test Category 2");
        categoryService.save(category2);

        //product data
        Product testProduct1 = new Product();
        testProduct1.setName("Test Product 1");
        testProduct1.setPrice(200.0);
        testProduct1.setCategory(category1);
        testProduct1 = productService.saveProduct(testProduct1);

        Product testProduct2 = new Product();
        testProduct2.setName("Test Product 2");
        testProduct2.setPrice(150.0);
        testProduct2.setCategory(category1);
        testProduct2 = productService.saveProduct(testProduct2);

        Product testProduct3 = new Product();
        testProduct3.setName("Test Product 3");
        testProduct3.setPrice(350.0);
        testProduct3.setCategory(category2);
        testProduct3 = productService.saveProduct(testProduct3);

        Product testProduct4 = new Product();
        testProduct4.setName("Test Product 4");
        testProduct4.setPrice(400.0);
        testProduct4.setCategory(category2);
        testProduct4 = productService.saveProduct(testProduct4);

        Quantity q1 = new Quantity();
        q1.setValue(4);
        Quantity q2 = new Quantity();
        q2.setValue(3);
        Quantity q3 = new Quantity();
        q3.setValue(2);
        Quantity q4 = new Quantity();
        q4.setValue(1);

        //cart data
        Cart cart1 = new Cart();
        cart1.setCartItems(cartItemList);
        cart1.setUser(testUser);
        cartService.createCart(cart1);

        Cart cart2 = new Cart();
        cart2.setCartItems(cartItemList2);
        cart2.setUser(testUser2);
        cartService.createCart(cart2);

        //cartItems data
        CartItem cartItem1 = new CartItem();
        cartItem1.setProduct(testProduct1);
        cartItem1.setQuantity(q1);
        cartItem1.setCart(cart1);
        cartItemList.add(cartItem1);

        CartItem cartItem2 = new CartItem();
        cartItem2.setProduct(testProduct2);
        cartItem2.setQuantity(q2);
        cartItem2.setCart(cart1);
        cartItemList.add(cartItem2);
        cartItemService.createCartItemList(cartItemList);

        CartItem cartItem3 = new CartItem();
        cartItem3.setProduct(testProduct3);
        cartItem3.setQuantity(q3);
        cartItem3.setCart(cart2);
        cartItemList2.add(cartItem3);

        CartItem cartItem4 = new CartItem();
        cartItem4.setProduct(testProduct4);
        cartItem4.setQuantity(q4);
        cartItem4.setCart(cart2);
        cartItemList2.add(cartItem4);
        cartItemService.createCartItemList(cartItemList2);

        session.setAttribute("checkedCartItems", cartItemList);
        session.setAttribute("checkedCartItems", cartItemList2);
        session.setAttribute("userId", testUser.getId());

        Address testAddress = new Address();
        testAddress.setStreet("123 Test St1");
        testAddress.setCity("Test City1");
        testAddress.setState("TS1");
        testAddress.setZipCode("11111");
        testAddress.setUser(testUser);

        Address testAddress2 = new Address();
        testAddress2.setStreet("123 Test St2");
        testAddress2.setCity("Test City2");
        testAddress2.setState("TS2");
        testAddress2.setZipCode("22222");
        testAddress2.setUser(testUser);

        // Save the address using AddressService to get a valid ID
        Address savedAddress = addressService.createAddress(testAddress, testUser.getId());
        session.setAttribute("selectedAddressId", savedAddress.getId());

        Address savedAddress2 = addressService.createAddress(testAddress2, testUser.getId());
        session.setAttribute("selectedAddressId", savedAddress2.getId());

        session.setAttribute("paymentMethod", "Credit Card");

        model.addAttribute("message", "Test data set in session. You can now test your OrderController.");
        return "test-confirmation";
    }
}