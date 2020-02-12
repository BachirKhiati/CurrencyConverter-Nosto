package com.nosto.currencyconvertor.repositories;

import com.nosto.currencyconvertor.entities.CurrencyExchangeRate;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CurrencyExchangeRateRepository extends CrudRepository<CurrencyExchangeRate, String> {

    @Override
    List<CurrencyExchangeRate> findAll();
}


