package com.academy.Ecommerce.controller;


import com.academy.Ecommerce.DTO.ProcessPaymentRequest;
import com.academy.Ecommerce.DTO.ValidateCVCRequest;
import com.academy.Ecommerce.model.User;
import com.academy.Ecommerce.service.CardService;
import com.academy.Ecommerce.service.PaymentService;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
    public String getChooseCardForm(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        List<String> cardNumbers = cardService.getCardNumbersOfUser(user.getId()); // Assuming user ID 1
        ValidateCVCRequest validateCVCRequest = new ValidateCVCRequest(); // Ensure this matches your ValidateCVCRequest class

        model.addAttribute("cardNumbers", cardNumbers);
        model.addAttribute("validateCVCRequest", validateCVCRequest); // Add the validateCVCRequest to the model

        return "choose-card";
    }

    @PostMapping("/processPayment")
    public String processPayment(@ModelAttribute ValidateCVCRequest validateCVCRequest, BindingResult result, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Double totalPrice = (Double) session.getAttribute("totalPrice");
        ProcessPaymentRequest processPaymentRequest = new ProcessPaymentRequest(validateCVCRequest.getCardNumber(), totalPrice.longValue());
        if (result.hasErrors()) {
            List<String> cardNumbers = cardService.getCardNumbersOfUser(user.getId());
            model.addAttribute("cardNumbers", cardNumbers);
            model.addAttribute("validateCVCRequest", validateCVCRequest);
            return "choose-card";
        }

        try {
            paymentService.processPayment(validateCVCRequest, processPaymentRequest);
            System.out.println("Done and balance reduced!");
            return "redirect:/api/v1/orders/cartItems";  // Redirect to cartItems after successful payment
        } catch (FeignException e) {
            // Handle error and populate model with necessary attributes for the choose-card.html
            List<String> cardNumbers = cardService.getCardNumbersOfUser(user.getId()); // Assuming user ID 1
            model.addAttribute("cardNumbers", cardNumbers);
            model.addAttribute("validateCVCRequest", validateCVCRequest);
            model.addAttribute("processPaymentRequest", processPaymentRequest);

            if (e.getMessage().contains("Insufficient funds!")) {
                model.addAttribute("errorMessage", "Insufficient funds!");
            } else if (e.getMessage().contains("CVC card is wrong!")) {
                model.addAttribute("errorMessage", "CVC card is wrong!");
            }
            System.out.println(e.getMessage());
            return "choose-card"; // Return the same view with error messages
        } catch (Exception e) {
            List<String> cardNumbers = cardService.getCardNumbersOfUser(user.getId()); // Assuming user ID 1
            model.addAttribute("cardNumbers", cardNumbers);
            model.addAttribute("validateCVCRequest", validateCVCRequest);
            model.addAttribute("errorMessage", "An unexpected error occurred.");
            System.out.println(e.getMessage());
            return "choose-card"; // Return the same view with error messages
        }
    }



}
