package com.example.countries.data.source.local

import com.example.countries.data.Result
import com.example.countries.data.Result.Error
import com.example.countries.data.Result.Success
import com.example.countries.data.business.model.Country
import com.example.countries.data.source.CountriesRepository
import java.util.LinkedHashMap

/**
 * Implementation of a remote data source with static access to the data for easy testing.
 */
class FakeRepository : CountriesRepository {

    var countriesServiceData: LinkedHashMap<String, Country> = LinkedHashMap()

    private var shouldReturnError = false

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }

    fun add(list :List<Country>){
        list.forEach{
            countriesServiceData[it.countryId] = it
        }
    }

    override suspend fun getCountries(forceUpdate: Boolean): Result<List<Country>> {
        return if (shouldReturnError) {
            return Error(Exception("Test exception"))
        } else {
            Success(countriesServiceData.values.toList())
        }
    }

    override suspend fun getCountriesByName(
        name: String,
        forceUpdate: Boolean
    ): Result<List<Country>> {
        if (shouldReturnError) {
            return Error(Exception("Test exception"))
        }
        val item = countriesServiceData?.filter { name.contains(name) }
        return Success(item.values.toList())
    }

    override suspend fun getCountry(countryId: String, forceUpdate: Boolean): Result<Country> {
        if (shouldReturnError) {
            return Error(Exception("Test exception"))
        }
        countriesServiceData[countryId]?.let {
            return Success(it)
        }
        return Error(Exception("Could not find task"))
    }
}
