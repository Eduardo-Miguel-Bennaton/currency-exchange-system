package com.eduardo.currencyexchange.service;

import com.eduardo.currencyexchange.dto.ExchangeResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class ExchangeService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String EXCHANGE_API_URL = "https://api.exchangerate.host/convert?from={from}&to={to}&amount={amount}";

    public ExchangeResponse convertCurrency(String from, String to, double amount) {
        Map response = restTemplate.getForObject(EXCHANGE_API_URL, Map.class, from, to, amount);

        if (response == null || !response.containsKey("result") || !response.containsKey("info")) {
            throw new RuntimeException("Invalid response from exchange rate API");
        }

        double rate = ((Map<String, Double>) response.get("info")).get("rate");
        double converted = ((Number) response.get("result")).doubleValue();

        return new ExchangeResponse(
                from,
                to,
                amount,
                converted,
                rate,
                LocalDateTime.now()
        );
    }
}
