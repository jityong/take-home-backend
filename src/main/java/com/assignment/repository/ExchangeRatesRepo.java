package com.assignment.repository;

import com.assignment.model.Currency;
import lombok.Builder;
import lombok.Value;
import org.codejargon.fluentjdbc.api.mapper.Mappers;
import org.codejargon.fluentjdbc.api.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Repository
public class ExchangeRatesRepo {

    final private Query query;

    @Autowired
    public ExchangeRatesRepo(Query query) {
        this.query = query;
    }

    public void updateExchangeRates(final Currency.CurrencyEnum baseCurrency,
                                    final Map<Currency.CurrencyEnum, String> rates) {
        final String sql = "INSERT INTO exchange_rates(base_currency, target_currency, rate, retrieved_at)" +
                " VALUES(?,?,?,?)";
        final Timestamp timestamp = new Timestamp(Instant.now().toEpochMilli());
        final Stream<List<?>> params = rates.entrySet()
                .stream()
                .map(
                        e -> Arrays.asList(
                                baseCurrency.toString(),
                                e.getKey().toString(),
                                new BigDecimal(e.getValue()),
                                timestamp
                        )
                );

        query.batch(sql)
                .params(params)
                .run();
    }

    public BigDecimal getLatestRateForCurrency(final Currency.CurrencyEnum baseCurrency,
                                               final Currency.CurrencyEnum targetCurrency) {
        final String sql = "SELECT rate FROM exchange_rates e " +
                "WHERE e.base_currency = ? AND e.target_currency = ? " +
                "ORDER BY retrieved_at DESC LIMIT 1";
        return query.select(sql)
                .params(baseCurrency.toString(), targetCurrency.toString())
                .singleResult(Mappers.singleBigDecimal());
    }

    @Builder
    @Value
    public static class InsertExchangeRateParams {
        String baseCurrency;
        String targetCurrency;
        BigDecimal rate;
        Timestamp timestamp;
    }
}
