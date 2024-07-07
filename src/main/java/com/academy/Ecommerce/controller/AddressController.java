package com.academy.Ecommerce.controller;

import com.academy.Ecommerce.exception.NotFoundException;
import com.academy.Ecommerce.model.Address;
import com.academy.Ecommerce.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

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

    @GetMapping("/create")
    public String showAddressForm(Model model){
        model.addAttribute("address", new Address());
        return "address-form";
    }

    @PostMapping("/created")
    public String createAddress(@Valid @ModelAttribute Address address, BindingResult result){
        if(result.hasErrors()){
            return "address-form";
        }
        Address createdAddress = addressService.createAddress(address);
        Long userId = createdAddress.getUser().getId();
        return "redirect:/api/addresses/user/" + userId;
    }

    @GetMapping("/created")
    public String redirectToForm() {
        return "redirect:/api/addresses/create";
    }

    @GetMapping("/user/{userId}")
    public String getAddressesByUserId(@PathVariable Long userId, Model model){
        List<Address> addresses = addressService.getAddressesByUserId(userId);
        model.addAttribute("addresses", addresses);
        return "address-list";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Address address = addressService.getAddressById(id);
        model.addAttribute("address", address);
        return "address-update-form";
    }

    @PostMapping("/update/{id}")
    public String updateAddress(@Valid @PathVariable Long id, @ModelAttribute("address") Address address, BindingResult result) {
        if(result.hasErrors()){
            return "address-update-form";
        }
        addressService.updateAddressByUserId(address, id);
        return "redirect:/api/addresses/user/" + id;
    }

    @GetMapping("/delete/{id}")
    public String deleteAddress(@PathVariable Long id) {
        Address address = addressService.getAddressById(id);
        addressService.deleteAddressById(id);
        return "redirect:/api/addresses/user/" + address.getUser().getId();
    }
}