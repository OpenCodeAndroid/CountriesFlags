package com.example.countries.data.source

import com.example.countries.data.Result
import com.example.countries.data.business.model.Country

interface CountriesDataSource {
    suspend fun getCountries(): Result<List<Country>>
    suspend fun save(countryList: List<Country>)
    suspend fun deleteAllCountries()
}
