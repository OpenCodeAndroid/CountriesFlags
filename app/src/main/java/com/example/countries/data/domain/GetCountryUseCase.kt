package com.example.countries.data.domain

import com.example.countries.data.Result
import com.example.countries.data.business.model.Country
import com.example.countries.data.source.CountriesRepository
import com.example.countries.data.source.network.NetworkObserver
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class GetCountryUseCase(
    private val countriesRepository: CountriesRepository,
    private val networkObserver: NetworkObserver
) {
    @FlowPreview
    @ExperimentalCoroutinesApi
    suspend operator fun invoke(
        countryId: String,
        forceUpdate: Boolean = false
    ): Flow<Result<Country>> {

        return networkObserver.isConnectedFlow().debounce(100).map {
            countriesRepository.getCountry(countryId, it)
        }.distinctUntilChanged()
    }
}
