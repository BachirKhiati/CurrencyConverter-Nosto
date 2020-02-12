package com.nosto.currencyconvertor.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class CurrencyConverter {
    @NotNull(message = "JSON key 'to' is missing")
    @Size(min = 3, max = 3, message = "Invalid 'to' Currency value")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Invalid JSON 'to' value: Only Alphabet allowed")
    private String to;

    @NotNull(message = "JSON key 'from' is missing")
    @Size(min = 3, max = 3, message = "Invalid 'from' Currency value")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Invalid JSON 'from' value: Only Alphabet allowed")
    private String from;

    @NotNull(message = "JSON key 'value' is missing")
    @Pattern(regexp = "^(\\d+)([.]\\d+)?$", message = "Only Digits Allowed")
    private String value;

    public CurrencyConverter() {

    }

    public CurrencyConverter(String from, String to, String value) {
        this.from = from;
        this.to = to;
        this.value = value;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
