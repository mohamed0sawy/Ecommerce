package com.academy.Ecommerce.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Rating {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "product_id", nullable = false)
        private Product product;

        @ManyToOne
        @JoinColumn(name = "user_id", nullable = false)
        private User user;

        private Double rating;

    }


