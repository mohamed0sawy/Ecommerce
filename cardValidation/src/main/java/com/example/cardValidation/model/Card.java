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
    @Column(length = 16)
    private String number;

    @NonNull
    private Long pin;

    @NonNull
    @Column(length = 3)
    private Long cvc;

    @NonNull
    @Column(length = 2)
    private Long expMonth;

    @NonNull
    @Column(length = 2)
    private Long expYear;

    @Enumerated(EnumType.STRING)
    private CardStatus status;

}
