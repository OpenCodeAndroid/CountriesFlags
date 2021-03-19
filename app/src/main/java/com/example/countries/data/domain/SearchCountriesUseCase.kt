package com.example.countries.data.domain

import com.example.countries.data.Result
import com.example.countries.data.business.model.Country
import com.example.countries.data.source.CountriesRepository
import com.example.countries.data.source.network.NetworkObserver
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import timber.log.Timber

class SearchCountriesUseCase(
    private val countriesRepository: CountriesRepository,
    private val networkObserver: NetworkObserver,
    private var oneRemoteSuccess: Boolean = false
) {
    @FlowPreview
    @ExperimentalCoroutinesApi
    suspend operator fun invoke(
        name: String,
        forceUpdate: Boolean = false
    ): Flow<Result<List<Country>>> {
        // oneRemoteSuccess by invoke
        oneRemoteSuccess = false
        return flow {
            emit(countriesRepository.getCountriesByName(name, forceUpdate = false))

            while (!oneRemoteSuccess) {
                when (val result =
                    countriesRepository.getCountriesByName(name, forceUpdate = true)) {
                    // If there was a error get that from network (force true)
                    is Result.Error -> {
                        // Monitor when the device become on-line
                        networkObserver.isConnectedFlow()
                            .debounce(NetworkObserver.MILLISECONDS_DEBOUNCE_NETWORK_CHANGES)
                            .collect {
                                // When online try it
                                when (val newResult =
                                    countriesRepository.getCountriesByName(
                                        name,
                                        forceUpdate = forceUpdate
                                    )) {
                                    is Result.Error -> {
                                        Timber.e(
                                            newResult.exception,
                                            "the device is online but the remote communication still is nit there",
                                        )
                                    }
                                    Result.Loading -> {
                                        emit(newResult)
                                    }
                                    is Result.Success -> {
                                        oneRemoteSuccess = true
                                        emit(newResult)
                                    }
                                }
                            }
                    }
                    Result.Loading -> {
                        emit(result)
                    }
                    is Result.Success -> {
                        oneRemoteSuccess = true
                        emit(result)
                    }
                }
            }
        }.distinctUntilChanged()
    }
}
