package com.academy.Ecommerce.controller;


import com.academy.Ecommerce.DTO.ProcessPaymentRequest;
import com.academy.Ecommerce.DTO.ValidateCVCRequest;
import com.academy.Ecommerce.service.CardService;
import com.academy.Ecommerce.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final CardService cardService;
    private final PaymentService paymentService;


    @GetMapping
    public String showPaymentPage() {
        return "payment";
    }
    
    @GetMapping("/chooseCardForm")
    public String getChooseCardForm(Model model) {
        List<String> cardNumbers = cardService.getCardNumbersOfUser(1L); // Assuming user ID 1
        ValidateCVCRequest validateCVCRequest = new ValidateCVCRequest(); // Ensure this matches your ValidateCVCRequest class

        model.addAttribute("cardNumbers", cardNumbers);
        model.addAttribute("validateCVCRequest", validateCVCRequest); // Add the validateCVCRequest to the model

        return "choose-card";
    }

    @PostMapping("/processPayment")
    public String processPayment(@ModelAttribute ValidateCVCRequest validateCVCRequest, BindingResult result, Model model) {
        ProcessPaymentRequest processPaymentRequest = new ProcessPaymentRequest(validateCVCRequest.getCardNumber(), 100L);
        if (result.hasErrors()) {
            // Populate model with necessary attributes for the choose-card.html
            List<String> cardNumbers = cardService.getCardNumbersOfUser(1L); // Assuming user ID 1
            model.addAttribute("cardNumbers", cardNumbers);
            model.addAttribute("validateCVCRequest", validateCVCRequest);
            return "choose-card"; // Return the same view with error messages
        }

        // Call the service to validate CVC
        try {
            paymentService.processPayment(validateCVCRequest, processPaymentRequest);
            System.out.println("Done and balance reduced!");
            return "redirect:/api/v1/payment/chooseCardForm";
        } catch (Exception e) {
            // Handle error and populate model with necessary attributes for the choose-card.html
            List<String> cardNumbers = cardService.getCardNumbersOfUser(1L); // Assuming user ID 1
            model.addAttribute("cardNumbers", cardNumbers);
            model.addAttribute("validateCVCRequest", validateCVCRequest);
            model.addAttribute("processPaymentRequest", processPaymentRequest);
            model.addAttribute("errorMessage", e.getMessage());
//            model.addAttribute("errorMessage", "Balance Not Enough");
            System.out.println(e.getMessage());
            return "choose-card"; // Return the same view with error messages
        }
    }


}
