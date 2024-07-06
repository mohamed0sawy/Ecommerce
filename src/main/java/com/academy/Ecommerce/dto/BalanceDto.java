package com.academy.Ecommerce.dto;

import lombok.Data;

import java.util.List;

@Data
public class BalanceDto {
    private String object;
    private List<BalanceAmount> available;
    private List<BalanceAmount> connectReserved;
    private boolean livemode;
    private List<BalanceAmount> pending;
}
