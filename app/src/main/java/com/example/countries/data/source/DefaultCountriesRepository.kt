package com.example.countries.data.source

import com.example.countries.data.Result
import com.example.countries.data.business.model.Country

class DefaultCountriesRepository : CountriesRepository {
    override suspend fun getCountries(forceUpdate: Boolean): Result<List<Country>> {
        TODO("Not yet implemented")
    }

    override suspend fun save(countryList: List<Country>) {
        TODO("Not yet implemented")
    }
}
