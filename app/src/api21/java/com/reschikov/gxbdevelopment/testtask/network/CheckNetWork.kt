package com.reschikov.gxbdevelopment.testtask.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.LiveData

class CheckNetWork(context: Context ) : LiveData<Boolean>() {

    private val connectivity : ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networkCallback = object : ConnectivityManager.NetworkCallback(){
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            postValue(true)
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            postValue(false)
        }
    }

    override fun onActive() {
        super.onActive()
        startOff()
    }

    private fun startOff(){
        connectivity.registerNetworkCallback(createNetworkRequest(), networkCallback)
    }

    private fun createNetworkRequest() : NetworkRequest{
        return NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_VPN)
            .build()
    }

    override fun onInactive() {
        super.onInactive()
        complete()
    }

    private fun complete(){
        connectivity.unregisterNetworkCallback(networkCallback)
    }
}