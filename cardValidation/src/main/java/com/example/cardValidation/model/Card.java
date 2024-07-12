package com.example.cardValidation.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@Entity
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(length = 256, unique = true)
    private String cardNumberEncrypted;

    @NonNull
    @Column(length = 256)
    private String pinEncrypted;

    @NonNull
    @Column(length = 256)
    private String cvcEncrypted;

    @NonNull
    @Column(length = 2)
    private Long expMonth;

    @NonNull
    @Column(length = 2)
    private Long expYear;

    @Enumerated(EnumType.STRING)
    private CardStatus status;

    @Transient
    private String cardNumber; // Transient field to hold plain card number for processing
    @Transient
    private Long pin; // Transient field to hold plain pin for processing
    @Transient
    private Long cvc; // Transient field to hold plain cvc for processing
}

