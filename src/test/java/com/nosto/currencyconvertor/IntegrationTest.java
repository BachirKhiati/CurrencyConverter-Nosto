package com.nosto.currencyconvertor;

import com.nosto.currencyconvertor.entities.Currency;
import com.nosto.currencyconvertor.entities.CurrencyExchangeRate;
import com.nosto.currencyconvertor.models.CurrencyConverter;
import com.nosto.currencyconvertor.models.ExchangeOutput;
import com.nosto.currencyconvertor.repositories.CurrencyExchangeRateRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Profile("test")
class IntegrationTest {

    @LocalServerPort
    int port;

    private String basePath = "";


    @MockBean
    CurrencyExchangeRateRepository currencyExchangeRateRepository;

    @Autowired
    private TestRestTemplate restTemplate;


    @BeforeEach
    public void setup() {
        basePath = "http://localhost:" + port + "/";
        restTemplate = new TestRestTemplate();
        CurrencyExchangeRate currencyEUR = new CurrencyExchangeRate("EUR", 1);
        CurrencyExchangeRate currencyUSD = new CurrencyExchangeRate("USD", 1.2);
        when(currencyExchangeRateRepository.findById("EUR")).thenReturn(Optional.of(currencyEUR));
        when(currencyExchangeRateRepository.findById("USD")).thenReturn(Optional.of(currencyUSD));
    }


    @Test
    public void convert_Should_Be_Successful_Valid_Currency() {
        CurrencyConverter conversionCurrency = new CurrencyConverter("EUR", "USD", "1");
        ResponseEntity<ExchangeOutput> responseEntity = restTemplate.postForEntity(basePath + "convert", conversionCurrency, ExchangeOutput.class);
        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);

    }


    @Test
    public void convert_Test__Missing_Currency_Throw_Exception_Not_Found_() {
        Assertions.assertThrows(RestClientException.class, () -> {
            CurrencyConverter conversionCurrency = new CurrencyConverter("EUR", "FIN", "1");
            ResponseEntity<ExchangeOutput> responseEntity = restTemplate.postForEntity(basePath + "convert", conversionCurrency, ExchangeOutput.class);
        });
    }


    @Test
    public void getRate_Test_Should_Return_rates() {
        ResponseEntity<List<CurrencyExchangeRate>> responseEntity = restTemplate.exchange(basePath + "rates", HttpMethod.GET, null, new ParameterizedTypeReference<List<CurrencyExchangeRate>>() {
        });
        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }


    @Test
    public void currencies_Test_Should_Return_Currencies() {
        ResponseEntity<List<Currency>> responseEntity = restTemplate.exchange(basePath + "currencies", HttpMethod.GET, null, new ParameterizedTypeReference<List<Currency>>() {
        });
        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }


}
