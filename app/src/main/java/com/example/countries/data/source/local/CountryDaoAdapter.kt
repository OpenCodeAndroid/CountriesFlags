package com.example.countries.data.source.local

import com.example.countries.data.business.model.Country

object CountryDaoAdapter {
    fun mapToCountry(daoCountry: ModelDao.CountryWithCurrencies): Country =
        Country(
            countryId = daoCountry.countries.countryId,
            name = daoCountry.countries.name,
            capital = daoCountry.countries.capital,
            flagUrl = daoCountry.countries.flag,
            isoCode = daoCountry.countries.cioc,
            currencies = mapToCurrency(daoCountry.currencies)
        )

    fun mapToCurrency(currencyDaoList: List<ModelDao.Currency>): List<Country.Currency> =
        currencyDaoList.map { currencyDto ->
            Country.Currency(
                currencyId = currencyDto.currencyId,
                name = currencyDto.name,
                code = currencyDto.code,
                symbol = currencyDto.symbol
            )
        }

    fun mapToCountryDao(country: Country): ModelDao.CountryWithCurrencies =
        ModelDao.CountryWithCurrencies(
            ModelDao.Country(
                countryId = country.countryId,
                name = country.name,
                capital = country.capital,
                flag = country.flagUrl,
                cioc = country.isoCode
            ),
            currencies = mapToCurrencyDao(country.currencies)
        )

    fun mapToCurrencyDao(currencyList: List<Country.Currency>): List<ModelDao.Currency> =
        currencyList.map { currency ->
            ModelDao.Currency(
                currencyId = currency.currencyId,
                name = currency.name,
                code = currency.code,
                symbol = currency.symbol
            )
        }
}

fun ModelDao.CountryWithCurrencies.mapToModel() = CountryDaoAdapter.mapToCountry(this)
fun Country.mapToDao() = CountryDaoAdapter.mapToCountryDao(this)
