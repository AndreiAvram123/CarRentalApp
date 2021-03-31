package com.andrei.UI.helpers

import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.andrei.carrental.MainNavigationDirections
import com.andrei.carrental.R
import com.andrei.utils.isConnected
import com.andrei.utils.isNotConnected
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@ActivityScoped
class InternetConnectionHandler @Inject constructor(
        private val connectivityManager: ConnectivityManager) {

    private val _isConnected:MutableStateFlow<Boolean> = MutableStateFlow(
            connectivityManager.isConnected()
    )
    val isConnectedState:StateFlow<Boolean> = _isConnected.asStateFlow()

    fun isConnected() = connectivityManager.isConnected()


     private val networkCallback:ConnectivityManager.NetworkCallback = object : ConnectivityManager.NetworkCallback() {

         override fun onAvailable(network: Network) {
             _isConnected.tryEmit(true)
         }

         override fun onUnavailable() {
            _isConnected.tryEmit(false)
         }

         override fun onLost(network: Network) {
           _isConnected.tryEmit(false)
        }


    }


   fun stop(){
       connectivityManager.unregisterNetworkCallback(networkCallback)
   }
    fun start(){
        connectivityManager.registerDefaultNetworkCallback (networkCallback)
        _isConnected.tryEmit(connectivityManager.isConnected())
    }

}