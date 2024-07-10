package com.academy.Ecommerce.controller;

import com.academy.Ecommerce.exception.NotFoundException;
import com.academy.Ecommerce.model.Address;
import com.academy.Ecommerce.service.AddressService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;

@Controller
@RequestMapping("/api/v1/addresses")
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
    public String createAddress(@Valid @ModelAttribute Address address, BindingResult result,
                                HttpServletRequest request){
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            throw new NotFoundException("User ID not found in session");
        }
        if(result.hasErrors()){
            return "address-form";
        }
        addressService.createAddress(address, userId);
        return "redirect:/api/v1/addresses/user/" + userId;
    }

    @GetMapping("/created")
    public String redirectToForm() {
        return "redirect:/api/v1/addresses/create";
    }

    @GetMapping("/user/{userId}")
    public String getAddressesByUserId(@PathVariable Long userId, Model model){
        List<Address> addresses = addressService.getAddressesByUserId(userId);
        model.addAttribute("addresses", addresses);
        return "address-list";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            throw new NotFoundException("User ID not found in session");
        }
        Address address = addressService.getAddressByIdAndUserId(id, userId);
        if (address == null) {
            throw new NotFoundException("Address with ID " + id + " not found for User ID " + userId);
        }
        model.addAttribute("address", address);
        return "address-update-form";
    }

    @PostMapping("/update/{id}")
    public String updateAddress(@Valid @PathVariable Long id, @ModelAttribute("address") Address address, BindingResult result,
                                HttpServletRequest request) {
        if(result.hasErrors()){
            return "address-update-form";
        }
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            throw new NotFoundException("User ID not found in session");
        }
        addressService.updateAddressByUserId(address, id, userId);
        return "redirect:/api/v1/addresses/user/" + userId;
    }

    @GetMapping("/delete/{id}")
    public String deleteAddress(@PathVariable Long id) {
        Address address = addressService.getAddressById(id);
        addressService.deleteAddressById(id);
        return "redirect:/api/v1/addresses/user/" + address.getUser().getId();
    }
}