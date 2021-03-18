package com.example.countries.data.domain

import com.example.countries.data.Result
import com.example.countries.data.business.model.Country
import com.example.countries.data.source.CountriesRepository

class GetCountriesUseCase(
    private val countriesRepository: CountriesRepository
) {
    suspend operator fun invoke(forceUpdate: Boolean = false): Result<List<Country>> {
        return countriesRepository.getCountries(forceUpdate)
    }
}
