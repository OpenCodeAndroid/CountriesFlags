package com.example.countries.di

import androidx.room.Room
import com.example.countries.data.domain.GetCountriesUseCase
import com.example.countries.data.domain.GetCountryUseCase
import com.example.countries.data.domain.SearchCountriesUseCase
import com.example.countries.data.source.CountriesDataSource
import com.example.countries.data.source.CountriesRepository
import com.example.countries.data.source.DefaultCountriesRepository
import com.example.countries.data.source.local.CountriesDatabase
import com.example.countries.data.source.local.CountriesLocalDataSource
import com.example.countries.data.source.network.ApiHelper
import com.example.countries.data.source.network.NetworkObserver
import com.example.countries.data.source.network.RemoteDataSource
import com.example.countries.data.source.network.RetrofitBuilder
import com.example.countries.detail.CountryDetailViewModel
import com.example.countries.list.CountriesViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.dsl.module

object AppModule {
    val content = module {

        // Remote
        single { ApiHelper(RetrofitBuilder.apiService) }
        single<CountriesDataSource>(named<RemoteDataSource>()) {
            RemoteDataSource(api = get())
        }

        single { NetworkObserver(context = androidContext()) }

        // Local
        single(named<CountriesDatabase>()) { countriesDatabase() }
        single { get<CountriesDatabase>(named<CountriesDatabase>()).countryDao() }
        single<CountriesDataSource>(named<CountriesLocalDataSource>()) {
            CountriesLocalDataSource(
                countriesDao = get()
            )
        }

        single<CountriesRepository> {
            DefaultCountriesRepository(
                localDataSource = get(named<CountriesLocalDataSource>()),
                remoteDataSource = get(named<RemoteDataSource>())
            )
        }

        // UseCases
        factory { GetCountriesUseCase(get(), get()) }
        factory { GetCountryUseCase(get(), get()) }
        factory { SearchCountriesUseCase(get()) }

        viewModel { CountryDetailViewModel(getCountryUseCase = get()) }
        viewModel {
            CountriesViewModel(
                getCountriesUseCase = get(),
                searchCountriesUseCase = get()
            )
        }
    }
}

private fun Scope.countriesDatabase() =
    Room.databaseBuilder(androidContext(), CountriesDatabase::class.java, "Countries.db").build()
