package com.currency_converter.converter.service;

import com.currency_converter.converter.dto.CurrencyRequestDTO;
import com.currency_converter.converter.dto.CurrencyResponseDTO;

public interface CurrencyConverterService {
    public CurrencyResponseDTO convertCurrency(CurrencyRequestDTO requestDTO);
}