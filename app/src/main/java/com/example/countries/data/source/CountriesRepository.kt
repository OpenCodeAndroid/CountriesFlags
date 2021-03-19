package com.example.countries.data.source

import com.example.countries.data.Result
import com.example.countries.data.business.model.Country

interface CountriesRepository {
    suspend fun getCountries(forceUpdate: Boolean = false): Result<List<Country>>
    suspend fun getCountry(countryId: String, forceUpdate: Boolean = false): Result<Country>
    suspend fun getCountriesByName(name: String, forceUpdate: Boolean = false): Result<List<Country>>
}
