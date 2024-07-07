package com.academy.Ecommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/payment")
public class PaymentController {

    @GetMapping
    public String showPaymentPage() {
        return "payment";
    }
}
