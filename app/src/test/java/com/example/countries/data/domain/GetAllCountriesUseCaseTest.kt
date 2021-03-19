package com.example.countries.data.domain

import com.example.countries.data.Result.Error
import com.example.countries.data.Result.Success
import com.example.countries.data.business.model.Country
import com.example.countries.data.source.local.FakeRepository
import com.example.countries.data.source.network.NetworkObserver
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetAllCountriesUseCaseTest {

    private val countryRepository = FakeRepository()
    private val flow: Flow<Boolean> = flowOf()
    private val networkObserver = NetworkObserverFake(flow)
    private val useCase = GetAllCountriesUseCase(countryRepository, networkObserver)

    class NetworkObserverFake(val flow: Flow<Boolean>) : NetworkObserver {
        @ExperimentalCoroutinesApi
        override suspend fun isConnectedFlow() = flow
    }

    @Test
    fun loadTasks_error() = runBlockingTest {
        // Make the repository return errors
        countryRepository.setReturnError(true)

        // Load tasks
        val result = useCase()

        // Verify the result is an error
        assertTrue(result.take(1).first() is Error)
    }

    @Test
    fun loadTasks_noFilter() = runBlockingTest {
        // Given a repository with 1 active and 2 completed tasks:

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
            ),
            Country(
                countryId = "2",
                name = "name",
                flagUrl = "url",
                capital = "mind town",
                isoCode = "har",
                currencies = currencies
            ),
            Country(
                countryId = "3",
                name = "name",
                flagUrl = "url",
                capital = "mind town",
                isoCode = "har",
                currencies = currencies
            )
        )
        countryRepository.add(
            newCountry
        )

        // Load tasks
        val result = useCase()

        // Verify the result is filtered correctly
        assertTrue(result.take(1).first() is Success<*>)
        assertEquals((result.take(1).first() as Success<List<Country>>).data.size, 3)
    }
}
