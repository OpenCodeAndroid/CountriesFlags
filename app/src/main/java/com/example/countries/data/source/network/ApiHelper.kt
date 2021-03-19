package com.example.countries.data.source.network

import com.example.countries.data.source.network.dto.CountryDto

class ApiHelper(private val apiService: ApiService) {

    suspend fun getCountries() = apiService.getCountries()
    suspend fun getCountry(name: String) = apiService.getCountry(name)
    suspend fun getCountryByIso(isoCode: String): CountryDto = apiService.getCountryByIso(isoCode)
}
