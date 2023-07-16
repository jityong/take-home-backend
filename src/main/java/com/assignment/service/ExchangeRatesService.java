package com.assignment.service;

import com.assignment.model.Currency.CurrencyEnum;
import com.assignment.model.Currency.CurrencyTypeEnum;
import com.assignment.repository.ExchangeRatesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.assignment.model.Currency.CRYPTO_CURRENCY_ENUMS;
import static com.assignment.model.Currency.FIAT_CURRENCY_ENUMS;


@Service
public class ExchangeRatesService {
    private final ExchangeRatesRepo exchangeRatesRepo;

    @Autowired
    public ExchangeRatesService(ExchangeRatesRepo exchangeRatesRepo) {
        this.exchangeRatesRepo = exchangeRatesRepo;
    }

    public Map<CurrencyEnum, String> getExchangeRates(CurrencyEnum base, CurrencyTypeEnum currencyType) {
        List<CurrencyEnum> targetCurrenciesEnums = currencyType == CurrencyTypeEnum.FIAT ? CRYPTO_CURRENCY_ENUMS : FIAT_CURRENCY_ENUMS;
        final Map<CurrencyEnum, String> rates = new HashMap<>();

        for (CurrencyEnum target : targetCurrenciesEnums) {
            final BigDecimal rate = exchangeRatesRepo.getLatestRateForCurrency(base, target);
            rates.put(target, rate.toString());
        }

        return rates;
    }
}
