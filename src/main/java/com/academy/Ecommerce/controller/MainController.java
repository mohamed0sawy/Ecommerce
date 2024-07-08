package com.academy.Ecommerce.controller;

import com.academy.Ecommerce.model.Category;
import com.academy.Ecommerce.model.Product;
import com.academy.Ecommerce.service.CategoryService;
import com.academy.Ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
public class MainController {

    private final ProductService productService;
    private final CategoryService categoryService;


    @GetMapping("/")
    public String viewHomePage(@RequestParam(name = "page", defaultValue = "0") int page,
                              Model model) {
        int pageSize = 6;
        Page<Product> pagedProducts = productService.findAll(PageRequest.of(page, pageSize));
        model.addAttribute("categories", categoryService.getAllCategoryNames());


        model.addAttribute("pagedProducts", pagedProducts);

        return "main-page";
    }


    @GetMapping("/search")
    public String searchProducts(@RequestParam("keyword") String keyword,
                                 @RequestParam(name = "page", defaultValue = "0") int page,
                                 Model model) {
        int pageSize = 6;

        Page<Product> searchResultsPage = productService.searchProducts(keyword, PageRequest.of(page, pageSize));

        List<Product> searchResults = searchResultsPage.getContent();

        model.addAttribute("products", searchResults);
        model.addAttribute("categories", categoryService.getAllCategoryNames());
        model.addAttribute("pagedProducts", searchResultsPage); // Pass the Page object for pagination links

        return "main-page";
    }


    @GetMapping("/filter")
    public String filterProducts(@RequestParam("category") String category,
                                 @RequestParam(name = "page", defaultValue = "0") int page,
                                 Model model) {
        int pageSize = 6;

        Optional<Category> categoryObj= categoryService.findByName(category);
        if(categoryObj.isPresent()) {


            Page<Product> filteredProductsPage = productService.filterProductsByCategory(categoryObj.get(), PageRequest.of(page, pageSize));

            List<Product> filteredProducts = filteredProductsPage.getContent();

            model.addAttribute("products", filteredProducts);
            model.addAttribute("categories", categoryService.getAllCategoryNames());
            model.addAttribute("pagedProducts", filteredProductsPage); // Pass the Page object for pagination links

            return "main-page";
        }
        else{
            return "main-page";
        }
    }


}
