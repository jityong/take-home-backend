package com.assignment.service;

import com.assignment.model.Currency.CurrencyEnum;
import com.assignment.model.Currency.CurrencyTypeEnum;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.assignment.model.Currency.CRYPTO_CURRENCY_STRINGS;
import static com.assignment.model.Currency.FIAT_CURRENCY_STRINGS;


@Service
public class ExchangeRatesService {
    private final RestTemplate restTemplate;

    @Autowired
    public ExchangeRatesService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<CurrencyEnum, String> getExchangeRates(CurrencyEnum base, CurrencyTypeEnum currencyType) {
        final String API_URL = "https://api.coinbase.com/v2/exchange-rates?currency={currency}";
        DataResp response = restTemplate.getForObject(API_URL, DataResp.class, Collections.singletonMap("currency", base.toString()));
        List<CurrencyEnum> targetCurrenciesEnums = currencyType == CurrencyTypeEnum.FIAT ? CRYPTO_CURRENCY_STRINGS : FIAT_CURRENCY_STRINGS;
        // Convert to String List to compare easily
        List<String> targetCurrencies = targetCurrenciesEnums.stream().map(c -> c.toString()).collect(Collectors.toList());

        Map<CurrencyEnum, String> result = response.data.rates.entrySet()
                .stream()
                .filter(e -> targetCurrencies.contains(e.getKey()))
                .collect(Collectors.toMap(e -> CurrencyEnum.valueOf(e.getKey()), Map.Entry::getValue));

        return result;
    }

    @Jacksonized
    @Value
    @Builder
    public static class DataResp {
        private Data data;
    }

    @Jacksonized
    @Value
    @Builder
    public static class Data {
        private String currency;
        private Map<String, String> rates;
    }
}
