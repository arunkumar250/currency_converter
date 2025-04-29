package com.currency_converter.converter.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyRequestDTO {

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be a positive integer")
    private Integer amount;

    @NotNull(message = "Source currency is required")
    private String sourceCurrency;

    @NotNull(message = "Target currency is required")
    private String targetCurrency;
}

