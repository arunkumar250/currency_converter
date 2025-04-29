package com.currency_converter.converter.service.impl;

import com.currency_converter.converter.model.CurrencyRate;
import com.currency_converter.converter.repo.CurrencyRateRepository;
import com.currency_converter.converter.dto.CurrencyRequestDTO;
import com.currency_converter.converter.dto.CurrencyResponseDTO;
import com.currency_converter.converter.model.ConversionRequest;
import com.currency_converter.converter.repo.ConversionRequestRepository;
import com.currency_converter.converter.service.CurrencyConverterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
public class CurrencyConverterServiceImpl implements CurrencyConverterService {

    private static final String API_KEY = "1SMxQumaAcC996SUXGsnlBPW44t2RR82";
    private static final String API_URL = "https://api.currencybeacon.com/v1/convert?api_key=" + API_KEY;
    private static final Logger logger = LoggerFactory.getLogger(CurrencyConverterServiceImpl.class);

    @Autowired
    private CurrencyRateRepository currencyRateRepository;

    @Autowired
    private ConversionRequestRepository conversionRequestRepository;

    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public CurrencyResponseDTO convertCurrency(CurrencyRequestDTO requestDTO) {
    
        // Extracting from DTO
        String sourceCurrency = requestDTO.getSourceCurrency();
        String targetCurrency = requestDTO.getTargetCurrency();
        Integer amount = requestDTO.getAmount();
    
        // If source and target currencies are the same, return the same amount
        if (sourceCurrency.equals(targetCurrency)) {
            CurrencyResponseDTO response = new CurrencyResponseDTO();
            response.setConvertedAmount(Double.valueOf(amount));
            response.setSourceCurrency(sourceCurrency);
            response.setTargetCurrency(targetCurrency);
            return response;
        }
    
        // Try to fetch the conversion rate from the database for the last hour
        Optional<CurrencyRate> currencyRateOpt = currencyRateRepository.findBySourceCurrencyAndTargetCurrencyAndLastUpdatedAfter(
                sourceCurrency, targetCurrency, LocalDateTime.now().minusHours(1));
    
        Double conversionRate = null;
        if (currencyRateOpt.isPresent()) {
            conversionRate = currencyRateOpt.get().getRate();
            logger.info("Conversion rate fetched from database: " + conversionRate);
        }
    
        if (conversionRate == null) {
            String url = API_URL + "&from=" + sourceCurrency + "&to=" + targetCurrency + "&amount=" + amount;
            try {
                Map<String, Object> apiResponse = restTemplate.getForObject(url, Map.class);
    
                if (apiResponse != null && apiResponse.containsKey("response")) {
                    Map<String, Object> responseMap = (Map<String, Object>) apiResponse.get("response");
    
                    // Extract the conversion rate (value) from the response using the Map
                    conversionRate = Double.valueOf(responseMap.get("value").toString());  // Fixed casting issue
    
                    logger.info("Conversion rate fetched from Currency Beacon API: " + conversionRate);
    
                    // Save the fetched rate into the database
                    CurrencyRate newRate = new CurrencyRate();
                    newRate.setSourceCurrency(sourceCurrency);
                    newRate.setTargetCurrency(targetCurrency);
                    newRate.setRate(conversionRate);
                    newRate.setLastUpdated(LocalDateTime.now());
                    currencyRateRepository.save(newRate);
                } else {
                    logger.error("Failed to fetch a valid conversion rate from Currency Beacon API.");
                    throw new RuntimeException("Unable to fetch conversion rate.");
                }
            } catch (Exception e) {
                logger.error("Error while fetching conversion rate from Currency Beacon API: " + e.getMessage());
                throw new RuntimeException("Error during conversion. Please try again later.");
            }
        }
    
        // If conversionRate is still null, throw an exception or handle accordingly
        if (conversionRate == null) {
            logger.error("Conversion rate is null. Cannot perform conversion.");
            throw new RuntimeException("Conversion rate is not available.");
        }
    
        // Calculate the converted amount
        Double convertedAmount = amount * conversionRate;
    
        // Save the conversion request into the database
        ConversionRequest request = new ConversionRequest();
        request.setSourceCurrency(sourceCurrency);
        request.setTargetCurrency(targetCurrency);
        request.setAmount(amount);
        request.setConvertedAmount(convertedAmount);
        request.setRequestedAt(LocalDateTime.now());
        conversionRequestRepository.save(request);
    
        CurrencyResponseDTO response = new CurrencyResponseDTO();
        response.setConvertedAmount(convertedAmount);
        response.setSourceCurrency(sourceCurrency);
        response.setTargetCurrency(targetCurrency);
    
        return response;
    }
}
