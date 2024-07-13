package com.academy.Ecommerce.service;

import com.academy.Ecommerce.model.Product;
import com.academy.Ecommerce.model.Rating;
import com.academy.Ecommerce.model.User;
import com.academy.Ecommerce.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    public void rateProduct(Product product, Double rating, User user) {

            Rating newRating = new Rating();
            newRating.setProduct(product);
            newRating.setUser(user);
            newRating.setRating(rating);
            ratingRepository.save(newRating);
        }
}
