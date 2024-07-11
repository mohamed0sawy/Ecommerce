package com.example.cardPayment;

import com.example.cardPayment.model.CardBalance;
import com.example.cardPayment.repository.CardBalanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class CardPaymentApplication {

	public static void main(String[] args) {
		SpringApplication.run(CardPaymentApplication.class, args);
	}

}
