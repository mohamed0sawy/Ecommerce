package com.academy.Ecommerce.controller;

import com.academy.Ecommerce.model.Category;
import com.academy.Ecommerce.model.Product;
import com.academy.Ecommerce.service.CategoryService;
import com.academy.Ecommerce.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {


    private final ProductService productService;

    private final CategoryService categoryService;
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

        return "redirect:/products";
    }
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")

    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Product product = productService.getProductById(id);
        if(product == null){
           throw new IllegalArgumentException("Invalid product Id:" + id);

        }
        List<Category> categories = categoryService.findAll();
        model.addAttribute("product", product);
        model.addAttribute("categories", categories);
        return "product/edit";
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
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")

    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }
    @GetMapping("/{id}")
    public String showProductDetails(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        if (product == null) {
            throw new IllegalArgumentException("Invalid product Id:" + id);

        }
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.findAll());

        return "product/show";
    }

}