package com.academy.Ecommerce.controller;

import com.academy.Ecommerce.service.CategoryService;
import com.academy.Ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping("/")
    public String viewHomePage(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("categories", categoryService.getAllCategoryNames());
        return "mainPage";
    }

    @GetMapping("/search")
    public String searchProducts(@RequestParam("keyword") String keyword, Model model) {
        model.addAttribute("products", productService.searchProducts(keyword));
        model.addAttribute("categories", categoryService.getAllCategoryNames());
        return "mainPage";
    }

    @GetMapping("/filter")
    public String filterProducts(@RequestParam("category") String category, Model model) {
        model.addAttribute("products", productService.filterProductsByCategory(category));
        model.addAttribute("categories", categoryService.getAllCategoryNames());
        return "mainPage";
    }
}
