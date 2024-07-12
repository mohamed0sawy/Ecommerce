package com.example.cardPayment.config;

import com.example.cardPayment.model.CardBalance;
import com.example.cardPayment.service.CardBalanceService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CardBalanceService cardBalanceService;
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Override
    public void run(String... args) throws Exception {
        cardBalanceService.deleteCardBalances();

        logger.info("Creating and saving balance cards...");

        CardBalance balance1 = new CardBalance();
        balance1.setCardNumber("1234567890123456");
        balance1.setBalance(1000L);

        CardBalance balance2 = new CardBalance();
        balance2.setCardNumber("6543210987654321");
        balance2.setBalance(500L);

        CardBalance balance3 = new CardBalance();
        balance3.setCardNumber("1564148912347896");
        balance3.setBalance(30L);

        CardBalance balance4 = new CardBalance();
        balance4.setCardNumber("4567894523453298");
        balance4.setBalance(50L);

        CardBalance balance5 = new CardBalance();
        balance5.setCardNumber("9875125635412589");
        balance5.setBalance(5000L);

        cardBalanceService.saveCardBalance(balance1);
        cardBalanceService.saveCardBalance(balance2);
        cardBalanceService.saveCardBalance(balance3);
        cardBalanceService.saveCardBalance(balance4);
        cardBalanceService.saveCardBalance(balance5);

        logger.info("Finished creating and saving new card balances.");
    }
}
