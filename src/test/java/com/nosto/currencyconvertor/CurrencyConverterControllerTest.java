package com.nosto.currencyconvertor;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nosto.currencyconvertor.entities.CurrencyExchangeRate;
import com.nosto.currencyconvertor.repositories.CurrencyExchangeRateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Profile("test")
public class CurrencyConverterControllerTest {
    private static final ObjectMapper om = new ObjectMapper();
    @MockBean
    CurrencyExchangeRateRepository currencyExchangeRateRepository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CurrencyExchangeRate currencyEUR = new CurrencyExchangeRate("EUR", 1);
        CurrencyExchangeRate currencyUSD = new CurrencyExchangeRate("USD", 1.2);
        when(currencyExchangeRateRepository.findById("EUR")).thenReturn(Optional.of(currencyEUR));
        when(currencyExchangeRateRepository.findById("USD")).thenReturn(Optional.of(currencyUSD));
    }


    /*
            {
            "from": "EUR",
            "to": "USD",
            "timestamp": "2020-02-10T00:16:30.328436",
            "value": "100.0",
            "result": "109.69"
            }
    */
    @Test
    public void valueKeyValidationTestSuccessful200() throws Exception {
        String Json = "{" +
                "\"from\":\"EUR\", " +
                "\"to\":\"USD\", " +
                "\"value\":\"100\"}"; //<====

        mockMvc.perform(post("/convert")
                .content(Json)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("timestamp", is(notNullValue())))
                .andExpect(jsonPath("status", is(200)));
    }



    /*
        {
            "timestamp": "2020-02-09T21:31:16.154+0000",
            "status": 400,
            "errors": "Only Digits Allowed"
        }
    */
    @Test
    public void convertTestEmptyStringCurrencyValue400() throws Exception {
        String Json = "{\"from\":\"EUR\", \"to\":\"USD\", \"value\":\"\"}";
        mockMvc.perform(post("/convert")
                .content(Json)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("timestamp", is(notNullValue())))
                .andExpect(jsonPath("status", is(400)))
                .andExpect(jsonPath("errors").isString())
                .andExpect(jsonPath("errors", hasToString("Only Digits Allowed")));

    }


    /*
        {
            "timestamp": "2020-02-09T21:31:16.154+0000",
            "status": 400,
            "errors": "Only digits allowed"
        }
    */
    @Test
    public void valueKeyValidationTestNegative400() throws Exception {
        String Json = "{\"from\":\"EUR\", \"to\":\"USD\", \"value\":\"-10\"}";
        mockMvc.perform(post("/convert")
                .content(Json)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("timestamp", is(notNullValue())))
                .andExpect(jsonPath("status", is(400)))
                .andExpect(jsonPath("errors").isString())
                .andExpect(jsonPath("errors", hasToString("Only Digits Allowed")));

    }


    /*
    {
        "timestamp": "2020-02-09T21:40:31.481+0000",
        "status": 400,
        "errors": "JSON key 'value' is missing"
    }
*/
    @Test
    public void valueKeyValidationTestMissing400() throws Exception {
        String Json = "{" +
                "\"from\":\"EUR\", " +
                "\"to\":\"USD\"}";

        mockMvc.perform(post("/convert")
                .content(Json)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("timestamp", is(notNullValue())))
                .andExpect(jsonPath("status", is(400)))
                .andExpect(jsonPath("errors").isString())
                .andExpect(jsonPath("errors", hasToString("JSON key 'value' is missing")));

    }


    /*
        {
        "timestamp": "2020-02-09T21:40:31.481+0000",
        "status": 400,
        "errors": "Only Digits Allowed"
        }
    */
    @Test
    public void valueKeyValidationTestType400() throws Exception {
        String Json = "{" +
                "\"from\":\"EUR\", " +
                "\"to\":\"USD\", " +
                "\"value\":\"Finland100\"}"; //<====

        mockMvc.perform(post("/convert")
                .content(Json)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("timestamp", is(notNullValue())))
                .andExpect(jsonPath("status", is(400)))
                .andExpect(jsonPath("errors").isString())
                .andExpect(jsonPath("errors",
                        hasToString("Only Digits Allowed")));
    }





    /*
        {
        "timestamp": "2020-02-09T21:40:31.481+0000",
        "status": 400,
        "errors": "JSON key 'to' is missing"
        }
    */

    @Test
    public void from_to_Keys_Validation_Test_Missing400() throws Exception {
        String Json = "{" +
                "\"from\":\"EUR\", " +
//                    "\"to\":\"USD\", " + <===
                "\"value\":\"1\"}";
        mockMvc.perform(post("/convert")
                .content(Json)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("timestamp", is(notNullValue())))
                .andExpect(jsonPath("status", is(400)))
                .andExpect(jsonPath("errors").isString())
                .andExpect(jsonPath("errors",
                        hasToString("JSON key 'to' is missing")));
    }


        /*
        {
        "timestamp": "2020-02-09T21:40:31.481+0000",
        "status": 400,
        "Invalid 'from' Currency value"
        }
    */

    @Test
    public void from_to_Keys_Validation_Test_Length400() throws Exception {
        String Json = "{" +
                "\"from\":\"EURUUUUUU\", " + // <======
                "\"to\":\"USD\", " +
                "\"value\":\"1\"}";
        mockMvc.perform(post("/convert")
                .content(Json)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("timestamp", is(notNullValue())))
                .andExpect(jsonPath("status", is(400)))
                .andExpect(jsonPath("errors").isString())
                .andExpect(jsonPath("errors", hasToString("Invalid 'from' Currency value")));
    }
    /*
        {
        "timestamp": "2020-02-09T21:40:31.481+0000",
        "status": 400,
        "errors": "Invalid 'from' Currency value"
        }
    */

    @Test
    public void from_to_Keys_Validation_Test_Type400() throws Exception {
        String Json = "{" +
                "\"from\":\"EURUUUUUU\", " + // <======
                "\"to\":\"USD\", " +
                "\"value\":\"1\"}";
        mockMvc.perform(post("/convert")
                .content(Json)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("timestamp", is(notNullValue())))
                .andExpect(jsonPath("status", is(400)))
                .andExpect(jsonPath("errors").isString())
                .andExpect(jsonPath("errors", hasToString("Invalid 'from' Currency value")));
    }


    /*
            {
            "timestamp": "2020-02-10 12:25:05",
            "status": 400,
            "errors": "Malformed JSON request body"
            }
    */

    @Test
    public void body_Validation_Test_Malformed_400() throws Exception {
        String Json = "";
        mockMvc.perform(post("/convert")
                .content(Json)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("timestamp", is(notNullValue())))
                .andExpect(jsonPath("status", is(400)))
                .andExpect(jsonPath("errors").isString())
                .andExpect(jsonPath("errors", hasToString("Malformed JSON request body")));
    }




    /*
       {
        "timestamp": "2020-02-09T22:32:17.561+0000",
        "status": 400,
        "errors": "JSON key 'from' is missing || must not be blank || JSON key 'to' is missing || JSON key 'value' is missing || must not be blank"
    }
    */

    @Test
    public void body_Validation_Test_Empty_JSON_400() throws Exception {
        String Json = "{}";
        mockMvc.perform(post("/convert")
                .content(Json)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("timestamp", is(notNullValue())))
                .andExpect(jsonPath("status", is(400)))
                .andExpect(jsonPath("errors").isString());
    }


    /*
              {
            "timestamp": "2020-02-10 12:39:04",
            "status": 415,
            "errors": "Media type is not supported. Set header 'Content-Type' to 'application/json'"
        }
    */

    @Test
    public void header_Content_Type_Validation_Test_Missing() throws Exception {
        String Json = "{}";
        mockMvc.perform(post("/convert")
                .content(Json))
                //                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))<==
                .andDo(print())
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(jsonPath("timestamp", is(notNullValue())))
                .andExpect(jsonPath("status", is(415)))
                .andExpect(jsonPath("errors").isString())
                .andExpect(jsonPath("errors").isString())
                .andExpect(jsonPath("errors", hasToString("Media type is not supported. Set header 'Content-Type' to 'application/json'")));
    }


}
