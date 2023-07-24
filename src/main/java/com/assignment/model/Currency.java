package com.assignment.model;

import java.util.Arrays;
import java.util.List;

import static com.assignment.model.Currency.CurrencyEnum.*;

public class Currency {
    public static List<CurrencyEnum> FIAT_CURRENCY_ENUMS = Arrays.asList(USD, SGD, EUR);
    public static List<CurrencyEnum> CRYPTO_CURRENCY_ENUMS = Arrays.asList(BTC, DOGE, ETH);
    public enum CurrencyEnum {
        USD, SGD, EUR, BTC, DOGE, ETH;
    }
    public enum CurrencyTypeEnum {
        CRYPTO,
        FIAT;

    }
}
