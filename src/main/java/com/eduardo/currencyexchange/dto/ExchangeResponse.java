package com.eduardo.currencyexchange.dto;

import java.time.LocalDateTime;

public class ExchangeResponse {
    private String from;
    private String to;
    private double originalAmount;
    private double convertedAmount;
    private double rate;
    private LocalDateTime timestamp;

    // Constructors
    public ExchangeResponse() {}

    public ExchangeResponse(String from, String to, double originalAmount, double convertedAmount, double rate, LocalDateTime timestamp) {
        this.from = from;
        this.to = to;
        this.originalAmount = originalAmount;
        this.convertedAmount = convertedAmount;
        this.rate = rate;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public double getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(double originalAmount) {
        this.originalAmount = originalAmount;
    }

    public double getConvertedAmount() {
        return convertedAmount;
    }

    public void setConvertedAmount(double convertedAmount) {
        this.convertedAmount = convertedAmount;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
