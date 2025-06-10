package com.eduardo.currencyexchange.service;

import com.eduardo.currencyexchange.dto.ExchangeResponse;
import com.eduardo.currencyexchange.exception.ExternalApiException;
import com.eduardo.currencyexchange.exception.InvalidApiResponseException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

@Service
public class ExchangeService {

    private static final Logger logger = LoggerFactory.getLogger(ExchangeService.class);

    @Value("${exchange.api.base-url}")
    private String baseUrl;

    @Value("${exchange.api.access-key}")
    private String accessKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public ExchangeResponse convertCurrency(String from, String to, double amount) {
        String url = String.format("%s/latest?access_key=%s&base=EUR&symbols=%s,%s", baseUrl, accessKey, from, to);

        logger.info("Attempting to call external API URL with EUR base: {}", url);

        ResponseEntity<String> response = null;
        try {
            response = restTemplate.getForEntity(url, String.class);
        } catch (HttpClientErrorException e) {
            logger.error("API call failed with status code {}: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new ExternalApiException("Error calling exchange rate API: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            logger.error("An unexpected error occurred during API call: {}", e.getMessage(), e);
            throw new ExternalApiException("An unexpected error occurred when trying to get exchange rate.", e);
        }

        if (response == null || response.getBody() == null || response.getBody().isEmpty()) {
            logger.error("API call returned null or empty response body for URL: {}", url);
            throw new InvalidApiResponseException("API returned no data or an empty response body. Check API key/URL/parameters.");
        }

        String responseBody = response.getBody();
        logger.info("API Response Body: {}", responseBody);

        JSONObject json;
        try {
            json = new JSONObject(responseBody);
        } catch (Exception e) {
            logger.error("Failed to parse JSON response: {}", responseBody, e);
            throw new InvalidApiResponseException("Could not parse JSON response from API.", e);
        }

        if (!json.has("success") || !json.getBoolean("success")) {
            String errorMessage = "Unknown error from API.";
            if (json.has("error")) {
                errorMessage = json.getJSONObject("error").toString();
            }
            logger.error("API response indicates failure: {}", errorMessage);
            throw new ExternalApiException("Invalid response from exchange rate API: " + errorMessage);
        }

        JSONObject rates = json.getJSONObject("rates");
        if (!rates.has(from) || !rates.has(to)) {
            logger.error("API response missing expected rates for '{}' or '{}'. Response: {}", from, to, responseBody);
            throw new InvalidApiResponseException("API response missing expected exchange rates for " + from + " or " + to);
        }

        double rateFromEurToBase = rates.getDouble(from);
        double rateEurToTarget = rates.getDouble(to);
        double calculatedRate = rateEurToTarget / rateFromEurToBase;
        double convertedAmount = amount * calculatedRate;

        return new ExchangeResponse(
                from,
                to,
                amount,
                convertedAmount,
                calculatedRate,
                LocalDateTime.now()
        );
    }
}