package com.academy.Ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "product")

public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name cannot be longer than 100 characters")
    private String name;
    @Size(max = 255, message = "Description cannot be longer than 255 characters")
    private String description;
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    @NotNull(message = "Category is required")

    private Category category;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Price must be greater than or equal zero")
    private double price;
    @NotNull(message = "Stock is required")
    @Min(value = 0, message = "Stock must be greater than or equal to zero")
    private int stock;

    private String imageUrl;
    @OneToMany(mappedBy = "product")
    private List<Rating> ratings;
    public double getAverageRating() {
        if (ratings == null || ratings.isEmpty()) {
            return 0.0;
        }
        double average = ratings.stream().mapToDouble(Rating::getRating).average().orElse(0.0);
        BigDecimal roundedAverage = BigDecimal.valueOf(average).setScale(1, RoundingMode.HALF_UP);
        return roundedAverage.doubleValue();
    }





}