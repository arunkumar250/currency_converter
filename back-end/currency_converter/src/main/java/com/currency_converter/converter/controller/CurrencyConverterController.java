package com.currency_converter.converter.controller;

import com.currency_converter.converter.dto.CurrencyRequestDTO;
import com.currency_converter.converter.dto.CurrencyResponseDTO;
import com.currency_converter.converter.service.CurrencyConverterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/currency")
public class CurrencyConverterController {

    @Autowired
    private CurrencyConverterService currencyConverterService;

    @PostMapping("/convert")
    public ResponseEntity<CurrencyResponseDTO> convertCurrency(@Valid @RequestBody CurrencyRequestDTO requestDTO) {
        try {
            CurrencyResponseDTO responseDTO = currencyConverterService.convertCurrency(requestDTO);
            return ResponseEntity.ok(responseDTO);

        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(new CurrencyResponseDTO(
                    null, "Error", "Error converting currency"
            ));
        }
    }
}
