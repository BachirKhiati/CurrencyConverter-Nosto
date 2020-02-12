package com.nosto.currencyconvertor.error;

public class CurrencyNotFound extends RuntimeException {
    public CurrencyNotFound(String currency) {
        super("Currency not found : " + currency);
    }
}
