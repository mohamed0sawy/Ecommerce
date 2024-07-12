package com.academy.Ecommerce.service;

import com.academy.Ecommerce.model.Category;
import com.academy.Ecommerce.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    public List<String> getAllCategoryNames() {
        return categoryRepository.findAllCategoryNames();
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }
   public  Optional<Category> findByName(String categoryName){
        return categoryRepository.findByName(categoryName);
   }


    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }


}
