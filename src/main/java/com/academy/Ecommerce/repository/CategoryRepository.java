package com.academy.Ecommerce.repository;

import com.academy.Ecommerce.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    @Query("SELECT c.name FROM Category c")
    List<String> findAllCategoryNames();
    Optional<Category> findByName(String name);
}
