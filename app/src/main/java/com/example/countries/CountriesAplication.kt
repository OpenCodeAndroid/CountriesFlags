package com.example.countries

import android.app.Application
import com.example.countries.data.source.CountriesRepository
import com.example.countries.data.source.DefaultCountriesRepository
import com.example.countries.detail.CountryDetailViewModel
import com.example.countries.list.CountriesViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class CountriesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // https://insert-koin.io/docs/quickstart/android
        startKoin {
            androidLogger()
            androidContext(this@CountriesApplication)
            modules(appModule)
        }
    }
}

val appModule = module {
    // single instance of CountriesRepository
    single<CountriesRepository> { DefaultCountriesRepository() }
    viewModel { CountryDetailViewModel() }
    viewModel { CountriesViewModel() }
}
