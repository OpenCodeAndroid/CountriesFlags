package com.example.countries.data.source

import com.example.countries.data.Result
import com.example.countries.data.business.model.Country
import java.lang.Exception

interface CountriesDataSource {
    suspend fun getCountries(): Result<List<Country>>
    suspend fun getCountryByName(name: String): Result<Country>
    suspend fun getCountriesByName(name: String): Result<List<Country>>
    // TODO align interfaces or divide
    suspend fun getCountry(countryId: String): Result<Country> = Result.Error(Exception("Not implemented"))
    suspend fun save(countryList: List<Country>) { throw Exception("Not implemented") }
    suspend fun saveCountry(country: Country) { throw Exception("Not implemented") }
    suspend fun deleteAllCountries() { throw Exception("Not implemented") }
}
