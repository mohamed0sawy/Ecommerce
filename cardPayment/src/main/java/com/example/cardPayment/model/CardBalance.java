package com.example.cardPayment.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;


@Data
@NoArgsConstructor
@Entity
public class CardBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(length = 256, unique = true)
    private String cardNumberEncrypted;

    @NonNull
    private Long balance;

    @Transient
    private String cardNumber;
}