package com.example.countries.data.source.network

import com.example.countries.data.business.model.Country
import com.example.countries.data.source.network.dto.CountryDto
import com.example.countries.data.source.network.dto.CurrencyDto

object CountryDtoAdapter {
    fun mapToCountry(countryDto: CountryDto): Country =
        Country(
            name = countryDto.name,
            capital = countryDto.capital,
            flagUrl = countryDto.flag,
            isoCode = countryDto.cioc,
            currencies = mapToCurrency(countryDto.currencies)
        )

    fun mapToCurrency(currencyDtoList: List<CurrencyDto>): List<Country.Currency> =
        currencyDtoList.map { currencyDto ->
            Country.Currency(
                name = currencyDto.name,
                code = currencyDto.code,
                symbol = currencyDto.symbol
            )
        }
}

fun CountryDto.mapToModel() = CountryDtoAdapter.mapToCountry(this)
