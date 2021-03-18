package com.example.countries.data.source.network

import com.example.countries.data.source.network.dto.CountryDto
import retrofit2.http.GET

interface ApiService {

    @GET("all")
    suspend fun getCountries(): List<CountryDto>
}
