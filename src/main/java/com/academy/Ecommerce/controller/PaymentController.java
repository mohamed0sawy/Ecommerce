package com.academy.Ecommerce.controller;

import com.academy.Ecommerce.DTO.CardInfoDTO;
import com.academy.Ecommerce.feignClient.PaymentClientService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentClientService paymentClientService;

    @GetMapping
    public String showPaymentPage() {
        return "payment";
    }

    @GetMapping("/card")
    public String cardForm(Model model){
        model.addAttribute("cardInfoDTO", new CardInfoDTO());
        return "card-form";
    }

    @PostMapping("/card")
    public String cardFormData(@Valid @ModelAttribute("cardInfoDTO") CardInfoDTO cardInfoDTO,
                               BindingResult result, Model model, HttpServletRequest request){
        if (result.hasErrors()) {
            model.addAttribute("cardInfoDTO", new CardInfoDTO());
            return "card-form";
        }
        HttpSession session = request.getSession();
        Double totalPrice = (Double) session.getAttribute("totalPrice");
        cardInfoDTO.setTotalPrice(totalPrice);

//        System.out.println("total price : " + totalPrice.doubleValue());
//        System.out.println("card nom : " + cardInfoDTO.getCardNumber());
//        int month = Integer.parseInt(cardInfoDTO.getMonth());
//        System.out.println("card month : " + month);
//        return "card-form";

        HttpStatus httpStatus = paymentClientService.payWithCard(cardInfoDTO);

        if (httpStatus != HttpStatus.OK){
            return "redirect:/api/v1/payment/wentWrong";
        }
        return "redirect:/api/v1/order/newOrder";
    }

    @GetMapping("/wentWrong")
    public String wentWrong(){
        return "went-wrong";
    }
}
