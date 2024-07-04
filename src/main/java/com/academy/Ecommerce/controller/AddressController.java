package com.academy.Ecommerce.controller;

import com.academy.Ecommerce.model.Address;
import com.academy.Ecommerce.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

import java.util.List;

@Controller
@RequestMapping("/api/addresses")
public class AddressController {
    @Autowired
    AddressService addressService;

    @GetMapping
    public String getAllAddresses(Model model){
        List<Address> addresses = addressService.getAllAddresses();
        model.addAttribute("addresses", addresses);
        return "address-List";
    }

    @GetMapping("/new")
    public String showAddressForm(Model model){
        model.addAttribute("address", new Address());
        return "address-form";
    }

    @PostMapping("/create")
    public String createAddress(@ModelAttribute Address address){
        addressService.createAddress(address);
        return "redirect:/api/addresses";
    }
}