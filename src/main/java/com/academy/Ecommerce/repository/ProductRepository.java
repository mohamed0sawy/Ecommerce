package com.academy.Ecommerce.repository;

import com.academy.Ecommerce.model.Category;
import com.academy.Ecommerce.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByNameContaining(String keyword, Pageable pageable );
    List<Product> findByCategoryName(String categoryName);
    Page<Product> findByCategory(Category category, Pageable pageable);



}
