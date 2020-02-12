package com.nosto.currencyconvertor.models;

import java.util.Map;

public class CurrencyDTO {

    private Map<String, Double> rates;

    public CurrencyDTO() {
    }

    public Map<String, Double> getRates() {
        return rates;
    }

    public void setRates(Map<String, Double> rates) {
        this.rates = rates;
    }

}
