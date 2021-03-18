package com.example.countries

import android.app.Application
import androidx.room.Room
import com.example.countries.data.domain.GetCountriesUseCase
import com.example.countries.data.source.CountriesDataSource
import com.example.countries.data.source.CountriesRepository
import com.example.countries.data.source.DefaultCountriesRepository
import com.example.countries.data.source.local.CountriesDatabase
import com.example.countries.data.source.local.CountriesLocalDataSource
import com.example.countries.data.source.network.ApiHelper
import com.example.countries.data.source.network.RemoteDataSource
import com.example.countries.data.source.network.RetrofitBuilder
import com.example.countries.detail.CountryDetailViewModel
import com.example.countries.list.CountriesViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.dsl.module

class CountriesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@CountriesApplication)
            modules(appModule)
        }
    }
}

val appModule = module {

    // Remote
    single { ApiHelper(RetrofitBuilder.apiService) }
    single<CountriesDataSource>(named<RemoteDataSource>()) {
        RemoteDataSource(api = get())
    }

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

    // UseCase
    factory { GetCountriesUseCase(get()) }

    viewModel { CountryDetailViewModel() }
    viewModel { CountriesViewModel(getCountriesUseCase = get()) }
}

private fun Scope.countriesDatabase() =
    Room.databaseBuilder(androidContext(), CountriesDatabase::class.java, "Countries.db").build()
