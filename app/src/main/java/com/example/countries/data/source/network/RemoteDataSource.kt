package com.example.countries.data.source.network

import com.example.countries.data.Result
import com.example.countries.data.business.model.Country
import com.example.countries.data.source.CountriesDataSource

class RemoteDataSource(val api: ApiHelper) : CountriesDataSource {
    override suspend fun getCountries(): Result<List<Country>> {
        // TODO check error
        return Result.Success(api.getCountries().map { it.mapToModel() })
    }

    override suspend fun save(countryList: List<Country>) {
        // "Not necessary"
    }

    override suspend fun deleteAllCountries() {
        // "Not necessary"
    }
}
