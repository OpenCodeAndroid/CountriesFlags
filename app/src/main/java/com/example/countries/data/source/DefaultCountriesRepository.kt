package com.example.countries.data.source

import com.example.countries.data.Result
import com.example.countries.data.Result.Error
import com.example.countries.data.Result.Success
import com.example.countries.data.business.model.Country
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class DefaultCountriesRepository(
    private val localDataSource: CountriesDataSource,
    private val remoteDataSource: CountriesDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : CountriesRepository {

    private var cachedCountries: ConcurrentMap<String, Country>? = null

    override suspend fun getCountries(forceUpdate: Boolean): Result<List<Country>> {
        return withContext(ioDispatcher) {
            if (!forceUpdate) {
                cachedCountries?.let { cachedCountries ->
                    return@withContext Success(cachedCountries.values.toList())
                }
            }
            val newCountries = fetchCountriesFromRemoteOrLocal(forceUpdate)
            // Refresh the cache with the new tasks
            (newCountries as? Success)?.let { refreshCache(it.data) }

            cachedCountries?.values?.let { countries ->
                return@withContext Success(countries.toList())
            }

            (newCountries as? Success)?.let {
                if (it.data.isEmpty()) {
                    return@withContext Success(it.data)
                }
            }

            return@withContext Error(Exception("Illegal state"))
        }
    }

    override suspend fun save(countryList: List<Country>) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllCountries() {
        TODO("Not yet implemented")
    }

    private fun refreshCache(countries: List<Country>) {
        cachedCountries?.clear()
        countries.forEach {
            cacheAndPerform(it) {}
        }
    }

    private suspend fun fetchCountriesFromRemoteOrLocal(forceUpdate: Boolean): Result<List<Country>> {
        // Remote first
        when (val remoteCountries = remoteDataSource.getCountries()) {
            is Error -> Timber.w("Remote data source fetch failed")
            is Success -> {
                refreshLocalDataSource(remoteCountries.data)
                return remoteCountries
            }
            else -> throw IllegalStateException()
        }

        // Don't read from local if it's forced
        if (forceUpdate) {
            return Error(Exception("Can't force refresh: remote data source is unavailable"))
        }

        // Local if remote fails
        val localCountries = localDataSource.getCountries()
        if (localCountries is Success) return localCountries
        return Error(Exception("Error fetching from remote and local"))
    }

    private suspend fun refreshLocalDataSource(countries: List<Country>) {
        localDataSource.deleteAllCountries()
        localDataSource.save(countries)
    }

    private fun cacheAndPerform(country: Country, perform: (Country) -> Unit) {
        val cacheCountry = cacheCountry(country)
        perform(cacheCountry)
    }

    private fun cacheCountry(country: Country): Country {
        val newCountry = country.copy() // TODO check if it is necessary
        // Create if it doesn't exist.
        if (cachedCountries == null) {
            cachedCountries = ConcurrentHashMap()
        }
        cachedCountries?.put(newCountry.isoCode, newCountry)
        return newCountry
    }
}
