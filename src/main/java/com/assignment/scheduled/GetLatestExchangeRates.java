package com.assignment.scheduled;

import com.assignment.model.Currency;
import com.assignment.repository.ExchangeRatesRepo;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.assignment.model.Currency.CRYPTO_CURRENCY_ENUMS;
import static com.assignment.model.Currency.FIAT_CURRENCY_ENUMS;

@Component
public class GetLatestExchangeRates {
    private final RestTemplate restTemplate;
    private final ExchangeRatesRepo exchangeRatesRepo;

    public GetLatestExchangeRates(RestTemplate restTemplate, ExchangeRatesRepo exchangeRatesRepo) {
        this.restTemplate = restTemplate;
        this.exchangeRatesRepo = exchangeRatesRepo;
    }

    @Scheduled(fixedDelay = 1000)
    public void getLatestExchangeRates() {
        final String API_URL = "https://api.coinbase.com/v2/exchange-rates?currency={currency}";

        List<Currency.CurrencyEnum> allCurrencies = Stream.concat(CRYPTO_CURRENCY_ENUMS.stream(), FIAT_CURRENCY_ENUMS.stream()).collect(Collectors.toList());
        for (Currency.CurrencyEnum base : allCurrencies) {
            DataResp response = restTemplate.getForObject(API_URL, DataResp.class, Collections.singletonMap("currency", base.toString()));

            // Convert to String List to compare easily
            List<String> targetCurrencies = allCurrencies
                    .stream()
                    .map(c -> c.toString())
                    .collect(Collectors.toList());

            Map<Currency.CurrencyEnum, String> result = response.data.rates.entrySet()
                    .stream()
                    .filter(e -> targetCurrencies.contains(e.getKey()))
                    .collect(Collectors.toMap(e -> Currency.CurrencyEnum.valueOf(e.getKey()), Map.Entry::getValue));

            exchangeRatesRepo.updateExchangeRates(base, result);
            System.out.println(base);
            System.out.println(result);
        }
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
