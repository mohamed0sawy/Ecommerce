package com.academy.Ecommerce.controller;

import com.academy.Ecommerce.DTO.ValidationRequest;
import com.academy.Ecommerce.model.User;
import com.academy.Ecommerce.service.CardService;
import feign.FeignException;
import jakarta.mail.Session;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
            return "redirect:/api/v1/payment/chooseCardForm";
        } catch (FeignException e) {
            if (e.status() == 404) {
                model.addAttribute("errorMessage", "Card number is not valid!");
                System.out.println("Card number is not valid!");
            } else if (e.status() == 400) {
                model.addAttribute("errorMessage", "Card Information is wrong!");
                System.out.println("Card Information is wrong!");
            }
            return "add-card";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "An unexpected error occurred: " + e.getMessage());
            System.err.println("Error while processing card: " + e.getMessage());
            e.printStackTrace();
            return "add-card";
        }
    }


}
