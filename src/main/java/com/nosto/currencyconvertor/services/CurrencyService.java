package com.nosto.currencyconvertor.services;

import com.nosto.currencyconvertor.entities.Currency;
import com.nosto.currencyconvertor.entities.CurrencyExchangeRate;
import com.nosto.currencyconvertor.error.CurrencyNotFound;
import com.nosto.currencyconvertor.models.CurrencyConverter;
import com.nosto.currencyconvertor.models.ExchangeOutput;
import com.nosto.currencyconvertor.repositories.CurrencyExchangeRateRepository;
import com.nosto.currencyconvertor.repositories.CurrencyRepository;
import io.github.sercasti.tracing.core.Metric;
import io.github.sercasti.tracing.core.Tracing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class CurrencyService {


    private Tracing tracing;
    public CurrencyRepository currencyRepository;
    public CurrencyExchangeRateRepository currencyExchangeRateRepository;

    public CurrencyService(CurrencyRepository currencyRepository,
                           CurrencyExchangeRateRepository currencyExchangeRateRepository, Tracing tracing) {
        this.currencyRepository = currencyRepository;
        this.currencyExchangeRateRepository = currencyExchangeRateRepository;
        this.tracing = tracing;
    }

    public ExchangeOutput convert(CurrencyConverter currencyConverter) {
        Metric repoMetric1 = tracing.start("getRatesValueFromRepoDuration");
        String inputFrom = currencyConverter.getFrom().trim().toUpperCase();
        String inputTo = currencyConverter.getTo().trim().toUpperCase();
        double value = Double.parseDouble(currencyConverter.getValue());
        CurrencyExchangeRate toCurrency = this.currencyExchangeRateRepository.findById(inputTo).orElseThrow(() -> new CurrencyNotFound(inputTo));
        CurrencyExchangeRate fromCurrency = this.currencyExchangeRateRepository.findById(inputFrom).orElseThrow(() -> new CurrencyNotFound(inputFrom));
        repoMetric1.stop();
        Metric repoMetric2 = tracing.start("RateConversionDuration");
        double toValue = toCurrency.getValueInEuros();
        double fromValue = fromCurrency.getValueInEuros();
        double result = toValue * value / fromValue;
//        String currencyInput = getLocale(inputFrom, value);
//        String currencyOutput = getLocale(inputTo, result);
        ExchangeOutput output = new ExchangeOutput(
                fromCurrency.getName(),
                toCurrency.getName(),
                LocalDateTime.now(),
                value,
                result,
                200);
        repoMetric2.stop();

        return output;
    }

    public List<Currency> getCurrencies() {
        Metric repoMetric = tracing.start("getCurrenciesFromRepoDuration");
        List<Currency> currencyList = this.currencyRepository.findAll();
        repoMetric.stop();
        currencyList.sort(Comparator.comparing(Currency::getName));
        return currencyList;
    }

    public List<CurrencyExchangeRate> getRates() {
        Metric repoMetric = tracing.start("getRatesFromRepoDuration");
        List<CurrencyExchangeRate> currencyExchangeRateListList = this.currencyExchangeRateRepository.findAll();
        repoMetric.stop();
        currencyExchangeRateListList.sort(Comparator.comparing(CurrencyExchangeRate::getName));
        return currencyExchangeRateListList;
    }


    //##########################################
    // fetching locals from BE based on the currency and format the result
    // ##########################################
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
