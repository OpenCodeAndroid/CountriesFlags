package com.example.countries.data.source.network

class ApiHelper(private val apiService: ApiService) {

    suspend fun getCountries() = apiService.getCountries()
}
