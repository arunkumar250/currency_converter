package com.currency_converter.converter.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CurrencyResponseDTO {

    public CurrencyResponseDTO(Object object, String string, String string2) {
    }
    private Double convertedAmount;
    private String sourceCurrency;
    private String targetCurrency;
}