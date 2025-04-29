package com.currency_converter.converter.repo;

import com.currency_converter.converter.model.CurrencyRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface CurrencyRateRepository extends JpaRepository<CurrencyRate, Integer> {
    Optional<CurrencyRate> findBySourceCurrencyAndTargetCurrency(String sourceCurrency, String targetCurrency);
    Optional<CurrencyRate> findBySourceCurrencyAndTargetCurrencyAndLastUpdatedAfter(
            String sourceCurrency, String targetCurrency, LocalDateTime lastUpdated);
}

