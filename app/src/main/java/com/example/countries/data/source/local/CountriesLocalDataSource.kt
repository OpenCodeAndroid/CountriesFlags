package com.example.countries.data.source.local

import com.example.countries.data.Result
import com.example.countries.data.business.model.Country
import com.example.countries.data.source.CountriesDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.NullPointerException

class CountriesLocalDataSource internal constructor(
    private val countriesDao: CountriesDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : CountriesDataSource {
    override suspend fun getCountries(): Result<List<Country>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(
                countriesDao.getCountriesWithCurrencies().map {
                    it.mapToModel()
                }
            )
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getCountryByName(name: String): Result<Country> {
        return withContext(ioDispatcher) {
            try {
                val country = countriesDao.getCountryByName(name)
                if (country.isEmpty()) {
                    Result.Error(NullPointerException("Local data source get failed"))
                } else {
                    Result.Success(country.first().mapToModel())
                }
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

    override suspend fun getCountriesByName(name: String): Result<List<Country>> {
        return withContext(ioDispatcher) {
            try {
                val country = countriesDao.getCountriesByName(name)
                if (country.isEmpty()) {
                    Result.Error(NullPointerException("Local data source get failed"))
                } else {
                    Result.Success(country)
                }
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

    override suspend fun getCountry(countryId: String): Result<Country> {
        return withContext(ioDispatcher) {
            try {
                val country = countriesDao.getCountryById(countryId)
                if (country == null) {
                    Result.Error(NullPointerException("Local data source get failed"))
                } else {
                    Result.Success(country)
                }
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

    override suspend fun save(countryList: List<Country>) {
        withContext(ioDispatcher) {
            countriesDao.save(countryList.map { it.mapToDao() })
        }
    }

    override suspend fun saveCountry(country: Country) {
        countriesDao.insertCountry(countryWithCurrencies = country.mapToDao())
    }

    override suspend fun deleteAllCountries() {
        countriesDao.deleteAll()
    }
}
