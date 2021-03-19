package com.example.countries.data.domain

import com.example.countries.data.Result
import com.example.countries.data.business.model.Country
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow

class HybridCountryLoadUseCase(
    val getAllCountriesUseCase: GetAllCountriesUseCase,
    val getCountriesByNameUseCase: GetCountriesByNameUseCase
) {
    lateinit var allFlow: Flow<Result<List<Country>>>
    lateinit var byNameFlow: Flow<Result<List<Country>>>

    @ExperimentalCoroutinesApi
    @FlowPreview
    suspend fun invoke(
        name: String = ""
    ): Flow<Result<List<Country>>> =
        if (name.isEmpty()) {
            getAllCountriesUseCase.invoke().also { allFlow = it }
        } else {
            getCountriesByNameUseCase.invoke(name).also { byNameFlow = it }
        }
}
