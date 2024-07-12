package com.example.cardValidation;

import com.example.cardValidation.model.Card;
import com.example.cardValidation.model.CardStatus;
import com.example.cardValidation.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@RequiredArgsConstructor
public class CardValidationApplication {

	public static void main(String[] args) {
		SpringApplication.run(CardValidationApplication.class, args);
	}

}