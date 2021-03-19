package com.example.countries.data.source.network

import com.example.countries.data.source.network.dto.CountryDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("all")
    suspend fun getCountries(): List<CountryDto>

    @GET("name/{nameValue}")
    suspend fun getCountry(@Path("nameValue")name: String): List<CountryDto>

    @GET("alpha/{isoCode}")
    suspend fun getCountryByIso(@Path("isoCode")isoCode: String): CountryDto
}
