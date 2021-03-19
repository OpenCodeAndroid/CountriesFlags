package com.example.countries.data.domain

import com.example.countries.data.Result
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

class NetworkComponent<T,R: Result<T>>(
    private val countriesRepository: CountriesRepository,
    private val networkObserver: NetworkObserver,
    private var oneRemoteSuccess: Boolean = false,

) {
    @FlowPreview
    @ExperimentalCoroutinesApi
    suspend operator fun invoke(
        forceUpdate: Boolean = false,
            getData : (Boolean)-> R
    ): Flow<Result<T>> {

        // oneRemoteSuccess by invoke
        oneRemoteSuccess = false
        return flow {

            // Start with the data that is available
            emit(getData(false))
            while (!oneRemoteSuccess) {

                // But lets be sure that that gets updated with the data that is available
                when (val result = getData(true)) {

                    // If there was a error get that from network (we know that by the use of force true)
                    is Result.Error -> {
                        // Monitor when the device become on-line
                        networkObserver.isConnectedFlow()
                            .debounce(MILLISECONDS_DEBOUNCE_NETWORK_CHANGES)
                            .collect {
                                // When online try it
                                when (val newResult =
                                    getData(forceUpdate)) {
                                    is Result.Error -> {
                                        Timber.e(
                                            newResult.exception,
                                            "the device is online but the remote communication still is nit there",
                                        )
                                    }
                                    Result.Loading -> {
                                        emit(newResult)
                                    }
                                    is Result.Success<*> -> {
                                        oneRemoteSuccess = true
                                        emit(newResult)
                                    }
                                }
                            }
                    }
                    Result.Loading -> {
                        emit(result)
                    }
                    is Result.Success<*> -> {
                        oneRemoteSuccess = true
                        emit(result)
                    }
                }
            }
        }   .distinctUntilChanged()
    }
}




