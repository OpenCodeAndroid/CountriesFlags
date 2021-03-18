package com.example.countries.data.source.network

import com.example.countries.data.Result
import com.example.countries.data.business.model.Country
import com.example.countries.data.source.CountriesDataSource

class RemoteDataSource(val api: ApiHelper) : CountriesDataSource {
    override suspend fun getCountries(): Result<List<Country>> {
        return try {
            Result.Success(api.getCountries().map { it.mapToModel() })
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getCountryByName(name: String): Result<Country> {
        return try {
            Result.Success(api.getCountry(name).first().mapToModel())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getCountry(countryId: String): Result<Country> {
        TODO("Not necessary")
    }

    override suspend fun save(countryList: List<Country>) {
        // "Not necessary"
    }

    override suspend fun saveCountry(country: Country) {
        // "Not necessary"
    }

    override suspend fun deleteAllCountries() {
        // "Not necessary"
    }
}
