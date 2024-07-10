package com.academy.Ecommerce.controller;

import com.academy.Ecommerce.DTO.ProcessPaymentRequest;
import com.academy.Ecommerce.DTO.ValidateCVCRequest;
import com.academy.Ecommerce.DTO.ValidationRequest;
import com.academy.Ecommerce.feignClient.PaymentClientService;
import com.academy.Ecommerce.feignClient.ValidationClientService;
import com.academy.Ecommerce.service.CardService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final ValidationClientService validationClientService;
    private final PaymentClientService paymentClientService;
    private final CardService cardService;

    @GetMapping
    public String showPaymentPage() {
        return "payment";
    }



    @GetMapping("/addCard")
    public String addCard(Model model) {
        model.addAttribute("validationRequest", new ValidationRequest());
        return "add-card";
    }

    @PostMapping("/addCard")
    public String addCard(@ModelAttribute("validationRequest") ValidationRequest validationRequest, BindingResult result, Model model, HttpServletRequest request) {
        if (result.hasErrors()) {
            return "add-card";
        }

        System.out.println(validationRequest.getNumber());
        try {
            cardService.validateCard(1L, validationRequest);
            System.out.println("Card Validated");
            return "redirect:/api/v1/payment/chooseCard";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "add-card";
        }
    }

    @GetMapping("/chooseCard")
    public String chooseCard(Model model) {
        List<String> cardNumbers = cardService.getCardNumbersOfUser(1L); // Assuming user ID 1
        ValidateCVCRequest validateCVCRequest = new ValidateCVCRequest(); // Ensure this matches your ValidateCVCRequest class

        model.addAttribute("cardNumbers", cardNumbers);
        model.addAttribute("validateCVCRequest", validateCVCRequest); // Add the validateCVCRequest to the model

        return "choose-card";
    }

    @PostMapping("/validateCVC")
    public String validateCVC(@ModelAttribute ValidateCVCRequest validateCVCRequest, BindingResult result, Model model) {
        System.out.println("in validateCVC");
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
            validationClientService.validateCVC(validateCVCRequest);
            paymentClientService.processPayment(processPaymentRequest);
            return "redirect:/api/v1/payment/chooseCard";
        } catch (Exception e) {
            // Handle error and populate model with necessary attributes for the choose-card.html
            List<String> cardNumbers = cardService.getCardNumbersOfUser(1L); // Assuming user ID 1
            model.addAttribute("cardNumbers", cardNumbers);
            model.addAttribute("validateCVCRequest", validateCVCRequest);
            model.addAttribute("processPaymentRequest", processPaymentRequest);
            model.addAttribute("errorMessage", "CVC card is wrong!");
            model.addAttribute("errorMessage", "Balance Not Enough");
            System.out.println(e.getMessage());
            return "choose-card"; // Return the same view with error messages
        }
    }


}
