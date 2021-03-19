package com.example.countries.data.domain

import com.example.countries.data.Result
import com.example.countries.data.business.model.Country
import com.example.countries.data.source.CountriesRepository
import com.example.countries.data.source.network.NetworkObserver
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow

class GetCountriesByNameUseCase(
    private val countriesRepository: CountriesRepository,
    networkObserver: NetworkObserver
) :
    NetworkComponentCase<List<Country>, Result<List<Country>>>(networkObserver) {

    @ExperimentalCoroutinesApi
    @FlowPreview
    suspend fun invoke(countryId: String, forceUpdate: Boolean = false): Flow<Result<List<Country>>> {
        return super.invoke(forceUpdate, countryId)
    }
    override suspend fun getData(parameter: String, forceUpdate: Boolean): Result<List<Country>> {
        return countriesRepository.getCountriesByName(parameter, forceUpdate)
    }
}
