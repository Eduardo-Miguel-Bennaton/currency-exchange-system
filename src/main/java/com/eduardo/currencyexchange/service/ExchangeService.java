package com.eduardo.currencyexchange.service;

import com.eduardo.currencyexchange.dto.ExchangeResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
public class ExchangeService {

    @Value("${exchange.api.base-url}")
    private String baseUrl;

    @Value("${exchange.api.access-key}")
    private String accessKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public ExchangeResponse convertCurrency(String from, String to, double amount) {
        String url = String.format("%s/latest?access_key=%s&base=%s&symbols=%s", baseUrl, accessKey, from, to);

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        JSONObject json = new JSONObject(response.getBody());

        if (!json.getBoolean("success")) {
            throw new RuntimeException("Invalid response from exchange rate API: " + json.getJSONObject("error").toString());
        }

        double rate = json.getJSONObject("rates").getDouble(to);
        double convertedAmount = amount * rate;

        return new ExchangeResponse(
                from,
                to,
                amount,
                convertedAmount,
                rate,
                LocalDateTime.now()
        );
    }
}
