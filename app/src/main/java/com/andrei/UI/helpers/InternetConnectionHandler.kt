package com.andrei.UI.helpers

import android.net.ConnectivityManager
import android.net.Network
import androidx.navigation.NavController
import com.andrei.carrental.MainNavigationDirections
import com.andrei.carrental.R
import com.andrei.utils.isNotConnected

class InternetConnectionHandler(private val navigationController: NavController,
                                private val connectivityManager: ConnectivityManager) {



     private val networkCallback:ConnectivityManager.NetworkCallback = object : ConnectivityManager.NetworkCallback() {

         override fun onUnavailable() {
            navigationToNoInternetFragment()
         }

         override fun onLost(network: Network) {
           navigationToNoInternetFragment()
        }
    }

    private fun navigationToNoInternetFragment(){
        val action = MainNavigationDirections.actionGlobalNoInternetFragment()
        navigationController.navigate(action)
    }

  init {
      connectivityManager.registerDefaultNetworkCallback (networkCallback)
      navigationController.addOnDestinationChangedListener { _, destination, _ ->
          //avoid infinite loop
          if(connectivityManager.isNotConnected()) {
              if (destination.id == R.id.noInternetFragment) {
                  return@addOnDestinationChangedListener
              } else {
                  navigationToNoInternetFragment()
              }
          }
      }
  }
}