package com.example.countries.data.domain

import com.example.countries.data.Result
import com.example.countries.data.business.model.Country
import com.example.countries.data.source.CountriesRepository
import com.example.countries.data.source.network.NetworkObserver
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow

class GetCountryUseCase(
    private val countriesRepository: CountriesRepository,
    networkObserver: NetworkObserver
) :
    NetworkComponentCase<Country, Result<Country>>(networkObserver) {

    @ExperimentalCoroutinesApi
    @FlowPreview
    suspend fun invoke(countryId: String, forceUpdate: Boolean = false): Flow<Result<Country>> {
        return super.invoke(forceUpdate, countryId)
    }

    override suspend fun getData(parameter: String, forceUpdate: Boolean): Result<Country> {
        return countriesRepository.getCountry(parameter, forceUpdate = forceUpdate)
    }
}
