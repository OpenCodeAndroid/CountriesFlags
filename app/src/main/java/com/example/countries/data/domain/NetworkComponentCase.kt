package com.example.countries.data.domain

import com.example.countries.data.Result
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

abstract class NetworkComponentCase<T, R : Result<T>>(
    private val networkObserver: NetworkObserver
) {
    private var oneRemoteSuccess: Boolean = false
    abstract suspend fun getData(parameter: String, forceUpdate: Boolean): R

    @FlowPreview
    @ExperimentalCoroutinesApi
    suspend operator fun invoke(
        forceUpdate: Boolean = false,
        parameter: String = "",
    ): Flow<Result<T>> {

        // oneRemoteSuccess by invoke
        oneRemoteSuccess = false
        return flow {

            // Start with the data that is available
            emit(getData(parameter, false))
            while (!oneRemoteSuccess) {

                // But lets be sure that that gets updated with the data that is available
                when (val result = getData(parameter, true)) {

                    // If there was a error get that from network (we know that by the use of force true)
                    is Result.Error -> {
                        // Monitor when the device become on-line
                        networkObserver.isConnectedFlow()
                            .debounce(MILLISECONDS_DEBOUNCE_NETWORK_CHANGES)
                            .collect {
                                // When online try it
                                when (val newResult =
                                    getData(parameter, forceUpdate)) {
                                    is Result.Error -> {
                                        // Try again from network
                                        Timber.e(
                                            newResult.exception,
                                            "the device is online but the remote communication still is nit there",
                                        )
                                    }
                                    Result.Loading -> {
                                        emit(newResult)
                                    }
                                    is Result.Success<*> -> {
                                        // Success from network
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
                        // Success from network
                        oneRemoteSuccess = true
                        emit(result)
                    }
                }
            }
        }.distinctUntilChanged()
    }
}




