package com.academy.Ecommerce.service;

import com.academy.Ecommerce.dto.BalanceAmount;
import com.academy.Ecommerce.dto.BalanceDto;
import com.academy.Ecommerce.dto.BalanceSourcesTypes;
import com.stripe.exception.StripeException;
import com.stripe.model.Balance;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    public BalanceDto retrieveBalance() throws StripeException {
        Balance balance = Balance.retrieve();
        return convertToDTO(balance);
    }

    private BalanceDto convertToDTO(Balance balance) {
        BalanceDto balanceDTO = new BalanceDto();
        balanceDTO.setObject(balance.getObject());
        balanceDTO.setAvailable(convertAmounts(balance.getAvailable()));
        balanceDTO.setConnectReserved(convertAmounts(balance.getConnectReserved()));
        balanceDTO.setLivemode(balance.getLivemode());
        balanceDTO.setPending(convertAmounts(balance.getPending()));
        return balanceDTO;
    }

    private List<BalanceAmount> convertAmounts(List<Balance.Money> amounts) {
        if (amounts == null) {
            return Collections.emptyList();
        }
        return amounts.stream().map(this::convertAmount).collect(Collectors.toList());
    }

    private BalanceAmount convertAmount(Balance.Money money) {
        BalanceAmount balanceAmount = new BalanceAmount();
        balanceAmount.setAmount(money.getAmount());
        balanceAmount.setCurrency(money.getCurrency());

        BalanceSourcesTypes sourceTypes = new BalanceSourcesTypes();
        sourceTypes.setCard(money.getSourceTypes().getCard());
        balanceAmount.setSourceTypes(sourceTypes);

        return balanceAmount;
    }

}
