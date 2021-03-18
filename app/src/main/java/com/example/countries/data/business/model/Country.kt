package com.example.countries.data.business.model

data class Country(
    val name: String,
    val capital: String,
    val currencies: List<Currency>,
    val flagUrl: String,
    val isoCode: String
) {
    val isEmpty get() = name.isEmpty() || capital.isEmpty() || flagUrl.isEmpty()

    data class Currency(
        val code: String,
        val name: String,
        val symbol: String
    )
}
