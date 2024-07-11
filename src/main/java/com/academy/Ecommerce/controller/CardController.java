package com.academy.Ecommerce.controller;

import com.academy.Ecommerce.DTO.ValidationRequest;
import com.academy.Ecommerce.model.User;
import com.academy.Ecommerce.service.CardService;
import jakarta.mail.Session;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/card")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;



    @GetMapping("/cardForm")
    public String getCardForm(Model model) {
        model.addAttribute("validationRequest", new ValidationRequest());
        return "add-card";
    }

    @PostMapping("/cardForm")
    public String addCard(@ModelAttribute("validationRequest") ValidationRequest validationRequest, BindingResult result, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (result.hasErrors()) {
            return "add-card";
        }

        System.out.println(validationRequest.getNumber());
        try {
            cardService.validateCard(user.getId(), validationRequest);
            System.out.println("Card Validated");
            cardService.addCardToUser(user.getId(), validationRequest.getNumber());
            System.out.println("Card added to user successfully");
            return "redirect:/api/v1/payment/chooseCard";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "add-card";
        }
    }



}
