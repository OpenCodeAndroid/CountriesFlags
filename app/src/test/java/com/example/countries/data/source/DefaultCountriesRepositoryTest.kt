package com.example.countries.data.source

import com.example.countries.data.Result.Success
import com.example.countries.data.business.model.Country
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test

class DefaultCountriesRepositoryTest {

    private val country1 = Country("Title1", "Description1", emptyList(), "url1", "1")
    private val country2 = Country("Title2", "Description2", emptyList(), "url2", "2")
    private val country3 = Country("Title3", "Description3", emptyList(), "url3", "3")
    private val newCountry = Country("Title new", "DescriptionNew", emptyList(), "urlNew", "New")
    private val remoteCountries = listOf(country1, country2)
    private val localCountries = listOf(country3)
    private val newCountries = listOf(country3)
    private lateinit var remoteDataSource: FakeDataSource
    private lateinit var localDataSource: FakeDataSource

    // Class under test
    private lateinit var countryRepository: CountriesRepository
    @ExperimentalCoroutinesApi
    @Before
    fun createRepository() {
        remoteDataSource = FakeDataSource(remoteCountries.toMutableList())
        localDataSource = FakeDataSource(localCountries.toMutableList())
        // Get a reference to the class under test
        countryRepository = DefaultCountriesRepository(
            localDataSource, remoteDataSource, Dispatchers.Unconfined
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getCountries_refreshesLocalDataSource() = runBlockingTest {
        val initialLocal = localDataSource.countries!!.toList()

        // First load will fetch from remote
        val newCountries = (countryRepository.getCountries() as Success).data

        assertEquals(remoteCountries, newCountries)
        assertEquals(remoteCountries, localDataSource.countries)
        assertNotEquals(initialLocal, localDataSource.countries)
    }
}
