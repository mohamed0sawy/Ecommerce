package com.academy.Ecommerce.controller;

import com.academy.Ecommerce.DTO.cardInfoDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/payment")
public class PaymentController {

    @GetMapping
    public String showPaymentPage() {
        return "payment";
    }

    @GetMapping("/card")
    public String cardForm(Model model){
        model.addAttribute("cardInfoDTO", new cardInfoDTO());
        return "card-form";
    }

    @PostMapping("/card")
    public String cardFormData(@Valid @ModelAttribute("cardInfoDTO") cardInfoDTO cardInfoDTO,
                               BindingResult result, Model model, HttpServletRequest request){
        if (result.hasErrors()) {
            model.addAttribute("cardInfoDTO", new cardInfoDTO());
            return "card-form";
        }
        HttpSession session = request.getSession();
//        Double totalPrice = (Double) session.getAttribute("totalPrice");
//        System.out.println("total price : " + totalPrice.doubleValue());
        System.out.println("card nom : " + cardInfoDTO.getCardNumber());
        int month = Integer.parseInt(cardInfoDTO.getMonth());
        System.out.println("card month : " + month);
        return "card-form";
    }
}
