package com.example.countries.data.domain

import com.example.countries.data.Result
import com.example.countries.data.business.model.Country
import com.example.countries.data.source.CountriesRepository
import com.example.countries.data.source.network.NetworkObserver
import com.example.countries.data.source.network.NetworkObserver.Companion.MILLISECONDS_DEBOUNCE_NETWORK_CHANGES
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class GetCountriesUseCase(
    private val countriesRepository: CountriesRepository,
    private val networkObserver: NetworkObserver
) {

    @FlowPreview
    @ExperimentalCoroutinesApi
    suspend operator fun invoke(
        forceUpdate: Boolean = false
    ): Flow<Result<List<Country>>> {
        return networkObserver.isConnectedFlow().debounce(MILLISECONDS_DEBOUNCE_NETWORK_CHANGES).map {
            countriesRepository.getCountries(forceUpdate)
        }.distinctUntilChanged()
    }
}
