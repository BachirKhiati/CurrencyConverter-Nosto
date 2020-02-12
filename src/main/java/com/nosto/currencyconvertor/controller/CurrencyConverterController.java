package com.nosto.currencyconvertor.controller;

import com.nosto.currencyconvertor.entities.Currency;
import com.nosto.currencyconvertor.entities.CurrencyExchangeRate;
import com.nosto.currencyconvertor.models.CurrencyConverter;
import com.nosto.currencyconvertor.models.ExchangeOutput;
import com.nosto.currencyconvertor.services.CurrencyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;


@RestController
@Validated
public class CurrencyConverterController {
    CurrencyService currencyService;

    public CurrencyConverterController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping(value = "/currencies", produces = {"application/json"})
    public ResponseEntity<List<Currency>> getAllCurrencies() {
        return new ResponseEntity<>(this.currencyService.getCurrencies(), HttpStatus.OK);
    }

    @GetMapping(value = "/rates", produces = {"application/json"})
    public ResponseEntity<List<CurrencyExchangeRate>> getRates() {
        return new ResponseEntity<>(this.currencyService.getRates(), HttpStatus.OK);
    }


    @PostMapping(value = "/convert", produces = {"application/json"})
    public ResponseEntity<ExchangeOutput> currencyConverter(@Valid @RequestBody CurrencyConverter currencyConverter) {
        return new ResponseEntity<>(this.currencyService.convert(currencyConverter), HttpStatus.OK);
    }


}
