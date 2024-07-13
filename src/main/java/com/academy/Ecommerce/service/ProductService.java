package com.academy.Ecommerce.service;


import com.academy.Ecommerce.model.Category;
import com.academy.Ecommerce.model.Product;
import com.academy.Ecommerce.model.Rating;
import com.academy.Ecommerce.repository.ProductRepository;
import com.academy.Ecommerce.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final RatingRepository ratingRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Optional<Product> findProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public void updateProduct(Long id, Product updatedProduct) {
        Product existingProduct = productRepository.findById(id).orElse(null);
        if (existingProduct != null) {
            existingProduct.setName(updatedProduct.getName());
            existingProduct.setDescription(updatedProduct.getDescription());
            existingProduct.setCategory(updatedProduct.getCategory());
            existingProduct.setPrice(updatedProduct.getPrice());
            existingProduct.setStock(updatedProduct.getStock());
            if (updatedProduct.getImageUrl() != null) {
                existingProduct.setImageUrl(updatedProduct.getImageUrl());
            }
            productRepository.save(existingProduct);
        }
    }

    public Page<Product> searchProducts(String keyword, Pageable pageable) {
        return productRepository.findByNameContaining(keyword,pageable);
    }


    public Page<Product> filterProductsByCategory(Category category, Pageable pageable) {
        return productRepository.findByCategory(category, pageable);
    }
    public double calculateAverageRating(Product product) {
        List<Rating> ratings = ratingRepository.findByProduct(product);
        return ratings.stream().mapToDouble(Rating::getRating).average().orElse(0.0);
    }


}