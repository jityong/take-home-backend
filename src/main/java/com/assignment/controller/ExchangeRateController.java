package com.assignment.controller;

import com.assignment.exceptions.InvalidBaseException;
import com.assignment.model.Currency.CurrencyEnum;
import com.assignment.model.Currency.CurrencyTypeEnum;
import com.assignment.service.ExchangeRatesService;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.assignment.model.Currency.CRYPTO_CURRENCY_ENUMS;
import static com.assignment.model.Currency.FIAT_CURRENCY_ENUMS;

@RestController
public class ExchangeRateController {
    private ExchangeRatesService exchangeRatesService;

    @Autowired
    public ExchangeRateController(ExchangeRatesService exchangeRatesService) {
        this.exchangeRatesService = exchangeRatesService;
    }

    @GetMapping("/rates")
    @ResponseBody
    public Map<CurrencyEnum, Map<CurrencyEnum, String>> getExchangeRates(@RequestParam String base) throws InvalidBaseException {
        base = base.toUpperCase();
        if (!EnumUtils.isValidEnum(CurrencyTypeEnum.class, base)) {
            throw new InvalidBaseException(base);
        }
        CurrencyTypeEnum baseType = CurrencyTypeEnum.valueOf(base);
        Map<CurrencyEnum, Map<CurrencyEnum, String>> result = new HashMap<>();
        List<CurrencyEnum> baseCurrencies = baseType == CurrencyTypeEnum.FIAT ? FIAT_CURRENCY_ENUMS : CRYPTO_CURRENCY_ENUMS;

        for (CurrencyEnum baseCurrency : baseCurrencies) {
            Map<CurrencyEnum, String> curr = exchangeRatesService.getExchangeRates(baseCurrency, baseType);
            result.put(baseCurrency, curr);
        }
        return result;
    }
}
