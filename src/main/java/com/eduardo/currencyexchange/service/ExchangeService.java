package com.eduardo.currencyexchange.service;

import com.eduardo.currencyexchange.dto.ExchangeResponse;
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
        // Option B: Adjusting for Fixer.io free tier limitation (base=EUR)
        // We always set base to EUR and request both 'from' and 'to' symbols
        String url = String.format("%s/latest?access_key=%s&base=EUR&symbols=%s,%s", baseUrl, accessKey, from, to);

        logger.info("Attempting to call external API URL with EUR base: {}", url);

        ResponseEntity<String> response = null;
        try {
            response = restTemplate.getForEntity(url, String.class);
        } catch (HttpClientErrorException e) {
            // This catches HTTP client errors (4xx status codes like 400, 401, 403, 404)
            logger.error("API call failed with status code {}: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Error calling exchange rate API: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            // Catches other potential exceptions like network issues, malformed URLs, etc.
            logger.error("An unexpected error occurred during API call: {}", e.getMessage(), e);
            throw new RuntimeException("An unexpected error occurred when trying to get exchange rate.", e);
        }

        if (response == null || response.getBody() == null || response.getBody().isEmpty()) {
            logger.error("API call returned null or empty response body for URL: {}", url);
            // Throw a specific exception if the body is null or empty
            throw new RuntimeException("API returned no data or an empty response body. Check API key/URL/parameters.");
        }

        String responseBody = response.getBody();
        logger.info("API Response Body: {}", responseBody); // Log the actual response body

        JSONObject json;
        try {
            json = new JSONObject(responseBody);
        } catch (Exception e) {
            logger.error("Failed to parse JSON response: {}", responseBody, e);
            throw new RuntimeException("Could not parse JSON response from API.", e);
        }

        if (!json.has("success") || !json.getBoolean("success")) {
            String errorMessage = "Unknown error from API.";
            if (json.has("error")) {
                errorMessage = json.getJSONObject("error").toString();
            }
            logger.error("API response indicates failure: {}", errorMessage);
            throw new RuntimeException("Invalid response from exchange rate API: " + errorMessage);
        }

        JSONObject rates = json.getJSONObject("rates");

        // It's also good practice to check if "rates" and the "to" currency exist
        // Now we need both 'from' and 'to' currencies relative to EUR
        if (!rates.has(from) || !rates.has(to)) {
            logger.error("API response missing expected rates for '{}' or '{}'. Response: {}", from, to, responseBody);
            throw new RuntimeException("API response missing expected exchange rates for " + from + " or " + to);
        }

        double rateFromEurToBase = rates.getDouble(from); // e.g., if from=USD, this is EUR_to_USD rate
        double rateEurToTarget = rates.getDouble(to);     // e.g., if to=EUR, this is EUR_to_EUR (which is 1)

        // Calculate the cross-rate: (amount_in_base / rate_from_base_to_from_currency) * rate_from_base_to_to_currency
        // Since Fixer.io free tier uses EUR as base, rates are like 1 EUR = X USD, 1 EUR = Y JPY
        // To convert FROM (e.g., USD) TO (e.g., JPY):
        // 1. Convert FROM currency to EUR: amount / rateFromEurToBase
        // 2. Convert EUR to TO currency: (result from 1) * rateEurToTarget
        // So, the final rate (from_to_to) is effectively: rateEurToTarget / rateFromEurToBase
        double calculatedRate = rateEurToTarget / rateFromEurToBase;
        double convertedAmount = amount * calculatedRate;

        return new ExchangeResponse(
                from,
                to,
                amount,
                convertedAmount,
                calculatedRate, // Return the calculated cross-rate
                LocalDateTime.now()
        );
    }
}