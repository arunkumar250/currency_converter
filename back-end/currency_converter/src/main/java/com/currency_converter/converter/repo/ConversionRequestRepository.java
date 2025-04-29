package com.currency_converter.converter.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.currency_converter.converter.model.ConversionRequest;

public interface ConversionRequestRepository extends JpaRepository<ConversionRequest, Integer> {
}

