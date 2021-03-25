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
import javax.inject.Inject

@ActivityScoped
class InternetConnectionHandler @Inject constructor(
        private val connectivityManager: ConnectivityManager) {

    private val onAvailableCallbacks:MutableSet<()->Unit> = mutableSetOf()
    private val onUnavailableCallbacks:MutableSet<()->Unit> = mutableSetOf()


    fun onAvailable (callback : ()->Unit) {
        onAvailableCallbacks.remove (callback)
        onAvailableCallbacks.add(callback)
    }

    fun onUnavailable(callback: () -> Unit){
        onUnavailableCallbacks.remove (callback)
        onUnavailableCallbacks.add (callback)
    }
    fun isConnected():Boolean = connectivityManager.isConnected()
    fun isNotConnected():Boolean = !connectivityManager.isConnected()



     private val networkCallback:ConnectivityManager.NetworkCallback = object : ConnectivityManager.NetworkCallback() {

         override fun onAvailable(network: Network) {
             onAvailableCallbacks.forEach { it() }
         }

         override fun onUnavailable() {
             onUnavailableCallbacks.forEach { it() }
         }

         override fun onLost(network: Network) {
             onUnavailableCallbacks.forEach { it() }
        }
    }




   fun stop(){
       connectivityManager.unregisterNetworkCallback(networkCallback)
   }
    fun start(){
        connectivityManager.registerDefaultNetworkCallback (networkCallback)
    }

}