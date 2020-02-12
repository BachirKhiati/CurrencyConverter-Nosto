package com.nosto.currencyconvertor;

import com.nosto.currencyconvertor.entities.Currency;
import com.nosto.currencyconvertor.entities.CurrencyExchangeRate;
import com.nosto.currencyconvertor.models.CurrencyConverter;
import com.nosto.currencyconvertor.models.ExchangeOutput;
import com.nosto.currencyconvertor.repositories.CurrencyExchangeRateRepository;
import com.nosto.currencyconvertor.repositories.CurrencyRepository;
import com.nosto.currencyconvertor.services.CurrencyService;
import io.github.sercasti.tracing.core.Metric;
import io.github.sercasti.tracing.core.Tracing;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.ZonedDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;


@ExtendWith(SpringExtension.class)

@Profile("test")
public class CurrencyServiceTest {
    @Mock
    CurrencyRepository currencyRepository;

    @Mock
    CurrencyExchangeRateRepository currencyExchangeRateRepository;

    @Mock
    Tracing tracing;


    private String basePath = "http://localhost:8080/convert";

    @InjectMocks
    private CurrencyService currencyService;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        currencyService = new CurrencyService(currencyRepository, currencyExchangeRateRepository, tracing);
        Mockito.when(tracing.start("getRatesValueFromRepoDuration")).thenReturn(new Metric("test", "test"));
        Mockito.when(tracing.start("RateConversionDuration")).thenReturn(new Metric("test2", "test2"));
        Mockito.when(tracing.start("getCurrenciesFromRepoDuration")).thenReturn(new Metric("test3", "test3"));
        Mockito.when(tracing.start("getRatesFromRepoDuration")).thenReturn(new Metric("test4", "test4"));

    }



    @Test
    public void getCurrenciesTestEmpty() {
        Mockito.when(currencyRepository.findAll()).thenReturn(Collections.emptyList());

        List<Currency> currencies = currencyService.getCurrencies();
        Assertions.assertTrue(currencies.isEmpty());
    }

    @Test
    public void getCurrenciesTestNullPointerException() throws NullPointerException {
        Assertions.assertThrows(NullPointerException.class, () -> {
            Mockito.when(currencyRepository.findAll()).thenReturn(null);
            List<Currency> currencies = currencyService.getCurrencies();
        });
    }

    @Test
    public void getCurrenciesTestSorted() {
        ArrayList<Currency> list = new ArrayList<>(Arrays.asList(
                new Currency("HTM"),
                new Currency("JSX"),
                new Currency("ASP"),
                new Currency("GPL")
        ));
        Mockito.when(currencyRepository.findAll()).thenReturn(list);
        List<Currency> currencies = currencyService.getCurrencies();
        // check if currencies are not empty
        Assertions.assertFalse(currencies.isEmpty());

        // Test if Sorted
        Assertions.assertEquals(currencies.get(0).getName(), "ASP");
        Assertions.assertEquals(currencies.get(1).getName(), "GPL");
        Assertions.assertEquals(currencies.get(2).getName(), "HTM");
        Assertions.assertEquals(currencies.get(3).getName(), "JSX");
    }


    @Test
    public void getRatesTestEmpty() {
        Mockito.when(currencyExchangeRateRepository.findAll()).thenReturn(Collections.emptyList());
        List<CurrencyExchangeRate> currencies = currencyService.getRates();
        Assertions.assertTrue(currencies.isEmpty());
    }

    @Test
    public void getRatesTestNullPointerException() throws NullPointerException {
        Assertions.assertThrows(NullPointerException.class, () -> {
            Mockito.when(currencyExchangeRateRepository.findAll()).thenReturn(null);
            List<CurrencyExchangeRate> currencies = currencyService.getRates();
        });
    }

    @Test
    public void getRatesTestSorted() {
        ArrayList<CurrencyExchangeRate> list = new ArrayList<>(Arrays.asList(
                new CurrencyExchangeRate("HTM", 1.5),
                new CurrencyExchangeRate("JSX", 2),
                new CurrencyExchangeRate("ASP", 7),
                new CurrencyExchangeRate("GPL", 9)
        ));
        Mockito.when(currencyExchangeRateRepository.findAll()).thenReturn(list);
        List<CurrencyExchangeRate> currencies = currencyService.getRates();
        // check if currencies are not empty
        Assertions.assertFalse(currencies.isEmpty());

        // Test if Sorted
        Assertions.assertEquals(currencies.get(0).getName(), "ASP");
        Assertions.assertEquals(currencies.get(1).getName(), "GPL");
        Assertions.assertEquals(currencies.get(2).getName(), "HTM");
        Assertions.assertEquals(currencies.get(3).getName(), "JSX");
    }


    @Test
    public void convertShouldReturnValue() {
        CurrencyExchangeRate currencyEUR = new CurrencyExchangeRate("EUR", 1);
        CurrencyExchangeRate currencyUSD = new CurrencyExchangeRate("USD", 1.2);
        Mockito.when(currencyExchangeRateRepository.findById("EUR")).thenReturn(Optional.of(currencyEUR));
        Mockito.when(currencyExchangeRateRepository.findById("USD")).thenReturn(Optional.of(currencyUSD));
        CurrencyConverter conversionCurrency = new CurrencyConverter("EUR", "USD", "1");
        ExchangeOutput result = this.currencyService.convert(conversionCurrency);
        Assertions.assertEquals(result.getStatus(), 200);
    }

}
