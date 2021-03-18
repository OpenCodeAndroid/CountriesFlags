package com.example.countries.data.domain

import com.example.countries.data.Result
import com.example.countries.data.business.model.Country
import com.example.countries.data.source.CountriesRepository

class GetCountryUseCase(
    private val countriesRepository: CountriesRepository
) {
    suspend operator fun invoke(countryId: String, forceUpdate: Boolean = false): Result<Country> {
        return countriesRepository.getCountry(countryId, forceUpdate)
    }
}
