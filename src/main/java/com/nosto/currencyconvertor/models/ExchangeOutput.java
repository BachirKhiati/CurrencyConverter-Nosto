package com.nosto.currencyconvertor.models;

import java.time.LocalDateTime;

public class ExchangeOutput {

    private String from;
    private String to;
    private LocalDateTime timestamp;
    private Double value;
    private Double result;


    private long status;

    public ExchangeOutput() {
    }

    public ExchangeOutput(String from, String to, LocalDateTime timestamp, Double value, Double result, long status) {
        this.from = from;
        this.to = to;
        this.timestamp = timestamp;
        this.value = value;
        this.result = result;
        this.status = status;
    }

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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Double getResult() {
        return result;
    }

    public void setResult(Double result) {
        this.result = result;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

}
