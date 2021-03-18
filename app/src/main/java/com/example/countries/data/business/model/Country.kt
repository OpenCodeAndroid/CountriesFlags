package com.example.countries.data.business.model

data class Country(
    val name: String,
    val capital: String,
    val currencies: List<Currency>,
    val flagUrl: String,
    val isoCode: String
) {
    data class Currency(
        val code: String,
        val name: String,
        val symbol: String
    )
}
