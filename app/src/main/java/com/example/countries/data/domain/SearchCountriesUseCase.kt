package com.example.countries.data.domain

import com.example.countries.data.Result
import com.example.countries.data.business.model.Country
import com.example.countries.data.source.CountriesRepository

class SearchCountriesUseCase(
    private val countriesRepository: CountriesRepository
) {
    suspend operator fun invoke(name: String, forceUpdate: Boolean = false): Result<List<Country>> {
        return countriesRepository.getCountriesByName(name, forceUpdate)
    }
}
