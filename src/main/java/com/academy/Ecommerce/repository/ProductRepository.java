package com.academy.Ecommerce.repository;

import com.academy.Ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContaining(String keyword);
    List<Product> findByCategory(String category);

    @Query(value = "SELECT DISTINCT p.category FROM Product p")
    List<String> findAllCategories();
}
