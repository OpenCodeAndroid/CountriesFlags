package com.example.countries.data.domain

import com.example.countries.data.Result
import com.example.countries.data.business.model.Country
import com.example.countries.data.source.CountriesRepository
import com.example.countries.data.source.network.NetworkObserver
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class SearchCountriesUseCase(
    private val countriesRepository: CountriesRepository,
    private val networkObserver: NetworkObserver
) {

    @FlowPreview
    @ExperimentalCoroutinesApi
    suspend operator fun invoke(
        name: String,
        forceUpdate: Boolean = false // TODO check if remove it from here and keep the networkObserver command that
    ): Flow<Result<List<Country>>> = networkObserver
        .isConnectedFlow()
        .debounce(NetworkObserver.MILLISECONDS_DEBOUNCE_NETWORK_CHANGES)
        .map { isOnline ->
            countriesRepository.getCountriesByName(name, forceUpdate = isOnline)
        }
        .distinctUntilChanged()
}
