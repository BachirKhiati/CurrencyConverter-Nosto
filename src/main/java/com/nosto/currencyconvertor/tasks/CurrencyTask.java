package com.nosto.currencyconvertor.tasks;

import com.nosto.currencyconvertor.entities.Currency;
import com.nosto.currencyconvertor.entities.CurrencyExchangeRate;
import com.nosto.currencyconvertor.models.CurrencyDTO;
import com.nosto.currencyconvertor.repositories.CurrencyExchangeRateRepository;
import com.nosto.currencyconvertor.repositories.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CurrencyTask {

    @Autowired
    CurrencyRepository currencyRepository;
    @Autowired
    CurrencyExchangeRateRepository currencyExchangeRateRepository;
    RestTemplate restTemplate = new RestTemplate();
    @Value("${fixer.io.apiKey}")
    private String fixerIoApiKey;

    // Runs every 5 hour
    @Scheduled(fixedRate = 5 * 3600)
    public void getRatesTask() {
        try {
            CurrencyDTO dTO = restTemplate.getForObject(fixerIoApiKey, CurrencyDTO.class);
            assert dTO != null;
            dTO.getRates().forEach(this::saveData);
            this.currencyRepository.save(new Currency("EUR"));
            this.currencyExchangeRateRepository.save(new CurrencyExchangeRate("EUR", 1));
        } catch (AssertionError e) {
            System.out.println(e.getMessage());
        }
    }

    private void saveData(String key, double value) {
        Currency currency = new Currency(key);
        CurrencyExchangeRate currencyExchangeRate = new CurrencyExchangeRate(key, value);
        this.currencyRepository.save(currency);
        this.currencyExchangeRateRepository.save(currencyExchangeRate);
    }
}
