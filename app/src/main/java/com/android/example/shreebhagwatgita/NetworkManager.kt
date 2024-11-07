package com.android.example.shreebhagwatgita

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.LiveData

class NetworkManager(context: Context):LiveData<Boolean>() {

    override fun onActive() {
        super.onActive()
        checkNetwork()
    }

    private val connectivityManager=context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
   private val networkRequest = NetworkRequest.Builder().apply {
       addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
       addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
       addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
       addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
       addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
   }.build()

    private val networkCallback = object :ConnectivityManager.NetworkCallback(){
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            postValue(true)
        }

        override fun onUnavailable() {
            super.onUnavailable()
            postValue(false)
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            postValue(false)
        }
    }
    private fun checkNetwork() {
        if(connectivityManager.activeNetwork==null) postValue(false)
        connectivityManager.registerNetworkCallback(networkRequest,networkCallback)
    }

    override fun onInactive() {
        super.onInactive()
        releaseCheckingInternetConnectivity()
    }

    private fun releaseCheckingInternetConnectivity() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }


}