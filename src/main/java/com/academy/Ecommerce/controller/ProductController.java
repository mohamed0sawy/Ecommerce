package com.academy.Ecommerce.controller;

import com.academy.Ecommerce.model.Category;
import com.academy.Ecommerce.model.Product;
import com.academy.Ecommerce.model.Rating;
import com.academy.Ecommerce.model.User;
import com.academy.Ecommerce.service.CategoryService;
import com.academy.Ecommerce.service.ProductService;
import com.academy.Ecommerce.service.RatingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {


    private final ProductService productService;

    private final CategoryService categoryService;

    private final RatingService ratingService;
    private static String UPLOADED_FOLDER = "src/main/resources/static/images/";


    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")

    public String listProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "product/index";
    }

    @GetMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")

    public String showCreateForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.findAll());
        return "product/create";
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")

    public String createProduct(@ModelAttribute @Valid Product product, BindingResult bindingResult, @RequestParam("image") MultipartFile image, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            return "product/create";
        }
        if (image.isEmpty()) {
            model.addAttribute("imageError", "Please select an image to upload");
            model.addAttribute("categories", categoryService.findAll());
            return "product/create";
        }

        try {
            // Save the image file to the static/images directory
            Path uploadPath = Paths.get(UPLOADED_FOLDER);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            byte[] bytes = image.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + image.getOriginalFilename());
            Files.write(path, bytes);

            product.setImageUrl("/images/" + image.getOriginalFilename());
        } catch (IOException e) {
            e.printStackTrace();
        }

        productService.saveProduct(product);

        return "redirect:/api/v1/products";
    }
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")

    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Optional<Product> productOptional = productService.findProductById(id);
        if(productOptional.isPresent()){
            Product product = productOptional.get();
            List<Category> categories = categoryService.findAll();
            model.addAttribute("product", product);
            model.addAttribute("categories", categories);
            return "product/edit";

        }else{
            throw new IllegalArgumentException("Invalid product Id:" + id);
        }

    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")

    public String editProduct(@PathVariable("id") Long id, @ModelAttribute("product") @Valid Product product,BindingResult bindingResult,
                              @RequestParam("image") MultipartFile image, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            return "product/edit";
        }

        try {
            if (!image.isEmpty()) {
                byte[] bytes = image.getBytes();
                Path path = Paths.get(UPLOADED_FOLDER + image.getOriginalFilename());
                Files.write(path, bytes);
                product.setImageUrl("/images/" + image.getOriginalFilename());
            }
            productService.updateProduct(id, product);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/api/v1/products";
    }
    @PostMapping("/{id}/rate")
    public String rateProduct(@PathVariable Long id, @RequestParam Double rating, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User existingUser = (User) session.getAttribute("user");
        Long orderId= (Long) session.getAttribute("orderId");
        if (existingUser == null) {
            return "redirect:/api/v1/login";
        }
      Optional<Product> productOptional =productService.findProductById(id);
        if (productOptional.isPresent() ) {
        Product product= productOptional.get();
            ratingService.rateProduct(product,rating,existingUser);
        }
        if(orderId!=null){
            return "redirect:/api/v1/orders/details/" + orderId;

        }
        else{
            return "redirect:/api/v1/products/"+id;
        }
    }


    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")

    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return "redirect:/api/v1/products";
    }
    @GetMapping("/{id}")
    public String showProductDetails(@PathVariable Long id, Model model) {
        Optional<Product> productOptional = productService.findProductById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            model.addAttribute("product", product);
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("averageRating", productService.calculateAverageRating(product));
            return "product/show";

        }else{
            throw new IllegalArgumentException("Invalid product Id:" + id);
        }

    }

}