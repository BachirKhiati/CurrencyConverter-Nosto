package com.nosto.currencyconvertor;


import com.nosto.currencyconvertor.entities.CurrencyExchangeRate;
import com.nosto.currencyconvertor.repositories.CurrencyExchangeRateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("dev")
class SecuredControllerSpringBootIntegrationTestDev {

    @MockBean
    CurrencyExchangeRateRepository currencyExchangeRateRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();


        CurrencyExchangeRate currencyEUR = new CurrencyExchangeRate("EUR", 1);
        CurrencyExchangeRate currencyUSD = new CurrencyExchangeRate("USD", 1.2);
        Mockito.when(currencyExchangeRateRepository.findById("EUR")).thenReturn(Optional.of(currencyEUR));
        Mockito.when(currencyExchangeRateRepository.findById("USD")).thenReturn(Optional.of(currencyUSD));
    }

    @WithMockUser("spring")
    @Test
    public void givenAuthRequestOnPrivateService_shouldSucceedWith200() throws Exception {
        String Json = "{" +
                "\"from\":\"EUR\", " +
                "\"to\":\"USD\", " +
                "\"value\":\"100\"}";
        mvc.perform(post("/convert").contentType(MediaType.APPLICATION_JSON).content(Json))
                .andExpect(status().isOk());
    }
}

