package com.example.countries.data.source.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.os.Build.VERSION
import androidx.annotation.RequiresApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class NetworkObserver(val context: Context) {

    private fun network(isConnected: ((Boolean) -> Unit)): (() -> Unit) {
        lateinit var cancelable: (() -> Unit)
        val listener = ConnectivityManager.OnNetworkActiveListener {
            isConnected(true)
        }
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
            .addDefaultNetworkActiveListener(listener)
        if (VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            cancelable = networkO(isConnected)
        }
        return fun() {
            (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).removeDefaultNetworkActiveListener(listener)
            cancelable()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun networkO(isConnected: ((Boolean) -> Unit)): (() -> Unit) {

        val listener = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                isConnected(true)
            }

            override fun onUnavailable() {
                isConnected(false)
            }
        }

        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
            .registerDefaultNetworkCallback(listener)

        return fun() {
            (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).unregisterNetworkCallback(listener)
        }
    }

    @ExperimentalCoroutinesApi
    suspend fun isConnectedFlow(): Flow<Boolean> = callbackFlow<Boolean> {

        val close = network {
            sendBlocking(it)
        }
        awaitClose { close() }
    }
}
