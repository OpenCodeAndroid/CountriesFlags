package com.example.countries.data.source.local

import com.example.countries.data.Result
import com.example.countries.data.business.model.Country
import com.example.countries.data.source.CountriesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CountriesLocalDataSource internal constructor(
    private val countriesDao: CountriesDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : CountriesRepository {
    override suspend fun getCountries(forceUpdate: Boolean): Result< List<Country>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(countriesDao.getCountriesWithCurrencies().map {
                it.mapToModel()
            })
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun save(countryList: List<Country>) {
        withContext(ioDispatcher) {
        countriesDao.save(countryList.map { it.mapToDao() })
    } }
}
