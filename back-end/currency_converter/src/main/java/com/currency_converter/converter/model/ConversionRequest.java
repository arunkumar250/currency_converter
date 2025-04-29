package com.currency_converter.converter.model;

import lombok.Data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
@Data
public class ConversionRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String sourceCurrency;
    private String targetCurrency;
    private Integer amount;
    private Double convertedAmount;

    @Column(name = "requested_at")
    private LocalDateTime requestedAt;
}
