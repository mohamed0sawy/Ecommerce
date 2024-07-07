package com.academy.Ecommerce.controller;

import com.academy.Ecommerce.model.Address;
import com.academy.Ecommerce.model.CartItem;
import com.academy.Ecommerce.service.AddressService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/Address")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @GetMapping("/list")
    public String listUserAddress(@RequestParam("user_id") Long userId, Model model){
        List<Address> addressList = addressService.findAddressByUserId(userId);
        model.addAttribute("addressList", addressList);
        return "address-list";
    }

    @PostMapping("/select")
    public String selectAddress(@RequestParam("addressId") Long addressId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("selectedAddressId", addressId);
        return "redirect:/api/v1/payment";
    }}
