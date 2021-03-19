package com.example.countries.data.domain

import com.example.countries.data.Result
import com.example.countries.data.business.model.Country
import com.example.countries.data.source.CountriesRepository
import com.example.countries.data.source.network.NetworkObserver
import com.example.countries.data.source.network.NetworkObserver.Companion.MILLISECONDS_DEBOUNCE_NETWORK_CHANGES
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import timber.log.Timber

class GetCountryUseCase(
    private val countriesRepository: CountriesRepository,
    private val networkObserver: NetworkObserver,
    private var oneRemoteSuccess: Boolean = false
) {
    @FlowPreview
    @ExperimentalCoroutinesApi
    suspend operator fun invoke(
        countryId: String,
        forceUpdate: Boolean = false // TODO check if remove it from here and keep the networkObserver command that
    ): Flow<Result<Country>> {
        // oneRemoteSuccess by invoke
        return flow {
            emit(countriesRepository.getCountry(countryId, forceUpdate = false))
            while (!oneRemoteSuccess) {
                when (val result = countriesRepository.getCountry(countryId, forceUpdate = true)) {
                    // If there was a error get that from network (force true)
                    is Result.Error -> {
                        // Monitor when the device become on-line
                        networkObserver.isConnectedFlow()
                            .debounce(MILLISECONDS_DEBOUNCE_NETWORK_CHANGES)
                            .collect {
                                // When online try it
                                when (val newResult =
                                    countriesRepository.getCountry(
                                        countryId,
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
