package com.academy.Ecommerce.dto;

import lombok.Data;

import java.util.Map;

@Data
public class BalanceAmount {
    private long amount;
    private String currency;
    private BalanceSourcesTypes sourceTypes;
}
