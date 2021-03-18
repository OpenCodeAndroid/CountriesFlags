package com.example.countries.data.business.model

data class Country(
    val countryId: String,
    val name: String,
    val capital: String,
    val currencies: List<Currency>,
    val flagUrl: String,
    val isoCode: String
) {
    val isEmpty get() = name.isEmpty() || capital.isEmpty() || flagUrl.isEmpty()

    data class Currency(
        val currencyId: String,
        val code: String,
        val name: String,
        val symbol: String
    )
}
