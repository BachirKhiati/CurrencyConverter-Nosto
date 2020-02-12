package com.nosto.currencyconvertor.services;

import com.nosto.currencyconvertor.entities.Currency;
import com.nosto.currencyconvertor.entities.CurrencyExchangeRate;
import com.nosto.currencyconvertor.error.CurrencyNotFound;
import com.nosto.currencyconvertor.models.CurrencyConverter;
import com.nosto.currencyconvertor.models.ExchangeOutput;
import com.nosto.currencyconvertor.repositories.CurrencyExchangeRateRepository;
import com.nosto.currencyconvertor.repositories.CurrencyRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class CurrencyService {

    public CurrencyRepository currencyRepository;
    public CurrencyExchangeRateRepository currencyExchangeRateRepository;

    public CurrencyService(CurrencyRepository currencyRepository,
                           CurrencyExchangeRateRepository currencyExchangeRateRepository) {
        this.currencyRepository = currencyRepository;
        this.currencyExchangeRateRepository = currencyExchangeRateRepository;
    }

    public ExchangeOutput convert(CurrencyConverter currencyConverter) {

        String inputFrom = currencyConverter.getFrom().trim().toUpperCase();
        String inputTo = currencyConverter.getTo().trim().toUpperCase();
        double value = Double.parseDouble(currencyConverter.getValue());

        CurrencyExchangeRate toCurrency = this.currencyExchangeRateRepository.findById(inputTo).orElseThrow(() -> new CurrencyNotFound(inputTo));
        CurrencyExchangeRate fromCurrency = this.currencyExchangeRateRepository.findById(inputFrom).orElseThrow(() -> new CurrencyNotFound(inputFrom));

        double toValue = toCurrency.getValueInEuros();
        double fromValue = fromCurrency.getValueInEuros();
        double result = toValue * value / fromValue;
//        String currencyInput = getLocale(inputFrom, value);
//        String currencyOutput = getLocale(inputTo, result);
        return new ExchangeOutput(
                fromCurrency.getName(),
                toCurrency.getName(),
                LocalDateTime.now(),
                value,
                result,
                200);
    }

    public List<Currency> getCurrencies() {
        List<Currency> currencyList = this.currencyRepository.findAll();
        currencyList.sort(Comparator.comparing(Currency::getName));
        return currencyList;
    }

    public List<CurrencyExchangeRate> getRates() {
        List<CurrencyExchangeRate> currencyExchangeRateListList = this.currencyExchangeRateRepository.findAll();
        currencyExchangeRateListList.sort(Comparator.comparing(CurrencyExchangeRate::getName));
        return currencyExchangeRateListList;
    }

//    private String getLocale(String strCode, double value) {
//        for (Locale locale : NumberFormat.getAvailableLocales()) {
//            String code = NumberFormat.getCurrencyInstance(locale).getCurrency().getCurrencyCode();
//            if (strCode.equals(code)) {
//                NumberFormat currency = NumberFormat.getCurrencyInstance(locale);
//                return currency.format(value);
//            }
//        }
//        return Double.toString(value);
//    }


}
