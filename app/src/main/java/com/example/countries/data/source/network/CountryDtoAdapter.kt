package com.example.countries.data.source.network

import com.example.countries.data.business.model.Country
import com.example.countries.data.source.network.dto.CountryDto
import com.example.countries.data.source.network.dto.CurrencyDto

object CountryDtoAdapter {
    fun mapToCountry(countryDto: CountryDto): Country =
        Country(
            countryId = countryDto.hashCode().toString(),
            name = countryDto.name.orEmpty(),
            capital = countryDto.capital.orEmpty(),
            flagUrl = countryDto.flag.orEmpty(),
            isoCode = countryDto.cioc.orEmpty(),
            currencies = mapToCurrency(countryDto.currencies)
        )

    private fun mapToCurrency(currencyDtoList: List<CurrencyDto>): List<Country.Currency> =
        currencyDtoList.map { currencyDto ->
            Country.Currency(
                currencyId = currencyDto.hashCode().toString(),
                name = currencyDto.name.orEmpty(),
                code = currencyDto.code.orEmpty(),
                symbol = currencyDto.symbol.orEmpty()
            )
        }
}

fun CountryDto.mapToModel() = CountryDtoAdapter.mapToCountry(this)
