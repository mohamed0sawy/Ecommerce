package com.academy.Ecommerce.controller;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute("jakarta.servlet.error.status_code");
        String errorMessage;
        String errorTitle;

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                errorTitle = "Page Not Found";
                errorMessage = "The page you are looking for might have been removed, had its name changed, or is temporarily unavailable.";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                errorTitle = "Internal Server Error";
                errorMessage = "Something went wrong on our end. Please try again later.";
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                errorTitle = "Access Denied";
                errorMessage = "You do not have permission to access this page.";
            } else {
                errorTitle = "Error";
                errorMessage = "An unexpected error occurred.";
            }

            model.addAttribute("statusCode", statusCode);
            model.addAttribute("errorTitle", errorTitle);
            model.addAttribute("errorMessage", errorMessage);
        }

        return "error";
    }
}
