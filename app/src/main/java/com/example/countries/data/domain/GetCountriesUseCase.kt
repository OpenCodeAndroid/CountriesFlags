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
import timber.log.Timber

class GetCountriesUseCase(
    private val countriesRepository: CountriesRepository,
    private val networkObserver: NetworkObserver,
    private var oneRemoteSuccess: Boolean = false
) {

    @FlowPreview
    @ExperimentalCoroutinesApi
    suspend operator fun invoke(
        forceUpdate: Boolean = false
    ): Flow<Result<List<Country>>> {
        oneRemoteSuccess = false
        // oneRemoteSuccess by invoke
        return flow {
            emit(countriesRepository.getCountries(false))
            while (!oneRemoteSuccess) {
                when (val result = countriesRepository.getCountries(true)) {
                    // If there was a error get that from network (force true)
                    is Result.Error -> {
                        // Monitor when the device become on-line
                        networkObserver.isConnectedFlow()
                            .debounce(MILLISECONDS_DEBOUNCE_NETWORK_CHANGES)
                            .collect {
                                // When online try it
                                when (val newResult =
                                    countriesRepository.getCountries(forceUpdate)) {
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




