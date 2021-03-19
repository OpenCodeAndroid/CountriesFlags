package com.example.countries

import android.app.Application
import com.example.countries.di.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class CountriesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@CountriesApplication)
            modules(AppModule.content)
        }
    }

    // fun isOnline(): Boolean {
    //     val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    //     var isActive = connMgr.isDefaultNetworkActive
    //     connMgr.addDefaultNetworkActiveListener {
    //         isActive =true
    //     }
    //     return networkInfo?.isConnected == true
    // }
}
