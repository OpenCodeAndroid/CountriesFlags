package com.example.countries.data.source.local

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.MediumTest
import com.example.countries.MainCoroutineRule
import com.example.countries.data.Result
import com.example.countries.data.business.model.Country
import com.example.countries.data.succeeded
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Integration test for the CountriesDataSource.
 */
@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@MediumTest
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class CountriesLocalDataSourceTest : KoinTest {

    private lateinit var localDataSource: CountriesLocalDataSource
    private lateinit var database: CountriesDatabase

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            CountriesDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        localDataSource = CountriesLocalDataSource(database.countryDao(), Dispatchers.Main)
    }

    @After
    fun cleanUp() {
        database.close()
        stopKoin()
    }

    @Test
    fun saveCountry_retrievesCountry() = runBlockingTest {
        // GIVEN - a new country saved in the database

        val currencies = listOf(
            Country.Currency("id1", "0", "wise", "w"),
            Country.Currency("id2", "1", "experience", "e")
        )

        val newCountry = listOf(
            Country(
                countryId = "1",
                name = "name",
                flagUrl = "url",
                capital = "mind town",
                isoCode = "har",
                currencies = currencies
            )
        )

        localDataSource.save(newCountry)

        // WHEN  - Countries retrieved
        val result = localDataSource.getCountries()

        // THEN - Same country is returned
        assertThat(result.succeeded, CoreMatchers.`is`(true))
        result as Result.Success
        assertThat(result.data[0].countryId, CoreMatchers.`is`("1"))
        assertThat(result.data[0].isoCode, CoreMatchers.`is`("har"))
        assertThat(result.data[0].name, CoreMatchers.`is`("name"))
        assertThat(result.data[0].flagUrl, CoreMatchers.`is`("url"))
        assertThat(result.data[0].capital, CoreMatchers.`is`("mind town"))
        assertThat(result.data[0].currencies, CoreMatchers.`is`(currencies))

        assert(true)
    }

    @Test
    fun saveCountries_retrievesMultiple() = runBlockingTest {
        // GIVEN - 2 new country saved in the database

        val currencies = listOf(
            Country.Currency("id1", "0", "wise", "w"),
            Country.Currency("id2", "1", "experience", "e")
        )

        val newCountry = listOf(
            Country(
                countryId = "1",
                name = "name",
                flagUrl = "url",
                capital = "mind town",
                isoCode = "har",
                currencies = currencies
            )
        )

        val currencies2 = listOf(
            Country.Currency("id1", "0", "wise", "w"),
            Country.Currency("id2", "1", "experience", "e2")
        )

        val newCountry2 = listOf(
            Country(
                countryId = "2",
                name = "name",
                flagUrl = "url",
                capital = "mind town",
                isoCode = "har2",
                currencies = currencies2
            )
        )

        localDataSource.save(newCountry)
        localDataSource.save(newCountry2)

        // WHEN  - Countries retrieved
        val result = localDataSource.getCountries()

        // THEN - Same Countries is returned
        assertThat(result.succeeded, CoreMatchers.`is`(true))
        result as Result.Success
        assertThat(result.data[0].countryId, CoreMatchers.`is`("1"))
        assertThat(result.data[0].isoCode, CoreMatchers.`is`("har"))
        assertThat(result.data[0].name, CoreMatchers.`is`("name"))
        assertThat(result.data[0].flagUrl, CoreMatchers.`is`("url"))
        assertThat(result.data[0].capital, CoreMatchers.`is`("mind town"))
        assertThat(result.data[0].currencies, CoreMatchers.`is`(currencies2))

        assertThat(result.data[1].countryId, CoreMatchers.`is`("2"))
        assertThat(result.data[1].isoCode, CoreMatchers.`is`("har2"))
        assertThat(result.data[1].name, CoreMatchers.`is`("name"))
        assertThat(result.data[1].flagUrl, CoreMatchers.`is`("url"))
        assertThat(result.data[1].capital, CoreMatchers.`is`("mind town"))
        assertThat(result.data[1].currencies, CoreMatchers.`is`(currencies2))

        assert(true)
    }
}
