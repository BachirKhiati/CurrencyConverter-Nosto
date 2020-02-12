package com.nosto.currencyconvertor;

import com.nosto.currencyconvertor.entities.Currency;
import com.nosto.currencyconvertor.models.CurrencyDTO;
import com.nosto.currencyconvertor.repositories.CurrencyExchangeRateRepository;
import com.nosto.currencyconvertor.tasks.CurrencyTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;


@ExtendWith(SpringExtension.class)
@Profile("test")
public class TaskTest {


    private final String basePath = "http://localhost:8080/";
    @MockBean
    CurrencyExchangeRateRepository currencyExchangeRateRepository;
    @MockBean
    Currency currency;

    @Mock
    RestTemplate restTemplate = new RestTemplate();

    @InjectMocks
    private CurrencyTask currencyTask = new CurrencyTask();
    private MockRestServiceServer mockServer;

    @Value("${fixer.io.apiKey}")
    private String fixerIoApiKey;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void getRatesTask_Test_AssertionError() throws AssertionError {
        Mockito.when(restTemplate.getForEntity(fixerIoApiKey, CurrencyDTO.class))
                .thenThrow(new AssertionError("eroor woookr bitch"));
        currencyTask.getRatesTask();
    }


}
