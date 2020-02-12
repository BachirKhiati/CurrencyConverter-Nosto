package com.nosto.currencyconvertor.entities;


import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CurrencyExchangeRate {
    @Id
    private String name;
    private double valueInEuros;

    public CurrencyExchangeRate() {
    }

    public CurrencyExchangeRate(String name, double valueInEuros) {
        this.name = name;
        this.valueInEuros = valueInEuros;
    }

    public String getName() {
        return name;
    }

    public double getValueInEuros() {
        return valueInEuros;
    }

}
