package com.example.countries.data.domain

import com.example.countries.data.Result
import com.example.countries.data.business.model.Country
import com.example.countries.data.source.CountriesRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchCountriesUseCase(
    private val countriesRepository: CountriesRepository,
    private var needToRefresh: Boolean = false,
    private var delayToRetryMilliSeconds: Long = 1000L
) {
    var flow: Flow<Result<List<Country>>>? = null
    suspend operator fun invoke(
        name: String,
        forceUpdate: Boolean = false
    ): Flow<Result<List<Country>>> {
        needToRefresh = forceUpdate
        flow = flow {
            while (needToRefresh) {
                when (val result = countriesRepository.getCountriesByName(name, forceUpdate)) {
                    is Result.Error -> {
                        emit(result)
                        delay(delayToRetryMilliSeconds)
                    }
                    Result.Loading -> {
                        emit(result)
                        delay(delayToRetryMilliSeconds)
                    }
                    is Result.Success -> {
                        emit(result)
                        needToRefresh = false
                    }
                }
            }
        }
        return flow!!
    }
}
