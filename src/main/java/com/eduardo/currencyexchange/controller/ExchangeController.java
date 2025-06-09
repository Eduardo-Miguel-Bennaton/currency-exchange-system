package com.eduardo.currencyexchange.controller;

import com.eduardo.currencyexchange.dto.ExchangeResponse;
import com.eduardo.currencyexchange.service.ExchangeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/exchange")
public class ExchangeController {

    private final ExchangeService exchangeService;

    public ExchangeController(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    @GetMapping
    public ExchangeResponse getExchangeRate(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam double amount
    ) {
        return exchangeService.convertCurrency(from, to, amount);
    }
}
