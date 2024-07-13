package com.academy.Ecommerce.service;

import com.academy.Ecommerce.DTO.ValidateCVCRequest;
import com.academy.Ecommerce.DTO.ValidateCVCRequestEnc;
import com.academy.Ecommerce.DTO.ValidationRequest;
import com.academy.Ecommerce.DTO.ValidationRequestEnc;
import com.academy.Ecommerce.feignClient.ValidationClient;
import com.academy.Ecommerce.model.Card;
import com.academy.Ecommerce.model.User;
import com.academy.Ecommerce.repository.CardRepository;
import com.academy.Ecommerce.utility.EncryptionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardService {

    private final ValidationClient validationClientService;
    private final CardRepository cardRepository;
    private final UserService userService;

    public void validateCard(Long userId, ValidationRequest validationRequest) throws Exception {
            ValidationRequestEnc validationRequestEnc = new ValidationRequestEnc(
                    EncryptionUtils.encrypt(validationRequest.getNumber()),
                    EncryptionUtils.encrypt(validationRequest.getPin().toString()),
                    EncryptionUtils.encrypt(validationRequest.getCvc().toString()),
                    validationRequest.getExpMonth(),
                    validationRequest.getExpYear());

            System.out.println("validationRequestEnc: " + validationRequestEnc);

            ResponseEntity<Void> responseEntity = validationClientService.validateCard(validationRequestEnc);

            if (responseEntity.getStatusCode() != HttpStatus.OK) {
                throw new RuntimeException("Failed to validate card");
            }

    }


    public void addCardToUser(Long userId, String cardNumber) {
        Card card = cardRepository.findByCardNumber(cardNumber);
        if( card == null) {
            card = new Card();
            card.setCardNumber(cardNumber);
            cardRepository.save(card);
        }
        User user = userService.findUserById(userId);
        List<Card> cards = user.getCards();
        if(cards.contains(card)) {
            //add logic to throw an error
            throw new RuntimeException("User already have this card");
        }
        cards.add(card);
        user.setCards(cards);
        userService.saveUser(user);
    }


    public List<String> getCardNumbersOfUser(Long userId) {
        User user = userService.findUserById(userId);
        List<Card> cards = user.getCards();
        List<String> cardNumbers = cards.stream()
                .map(Card::getCardNumber)
                .collect(Collectors.toList());
        return cardNumbers;
    }


}
