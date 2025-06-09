package com.eduardo.currencyexchange.controller;

import com.eduardo.currencyexchange.dto.ExchangeResponse;
import com.eduardo.currencyexchange.service.ExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exchange")
public class ExchangeController {

    private final ExchangeService exchangeService;

    @Autowired
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
