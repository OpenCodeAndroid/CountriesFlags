package com.example.countries.data.source

import com.example.countries.data.Result
import com.example.countries.data.business.model.Country

interface CountriesDataSource {
    suspend fun getCountries(): Result<List<Country>>
    suspend fun getCountryByName(name: String): Result<Country>
    suspend fun getCountriesByName(name: String): Result<List<Country>>
    suspend fun getCountryByIsoCode(isoCode: String): Result<Country>
    suspend fun getCountry(countryId: String): Result<Country>
    suspend fun save(countryList: List<Country>)
    suspend fun saveCountry(country: Country)
    suspend fun deleteAllCountries()
}
