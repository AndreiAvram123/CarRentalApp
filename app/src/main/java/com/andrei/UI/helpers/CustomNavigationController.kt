package com.andrei.UI.helpers


import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.andrei.carrental.MainNavigationDirections
import com.andrei.carrental.R

class CustomNavigationController(private val navigationController: NavController,
                                private val  internetConnectionHandler: InternetConnectionHandler) {


    private val destinationChangedListener:NavController.OnDestinationChangedListener =
            NavController.OnDestinationChangedListener { _, destination, _ ->
                if(internetConnectionHandler.isNotConnected()) {
                    if (destination.id != R.id.noInternetFragment) {
                        navigationToNoInternetFragment()
                    }
                }
            }


    private fun navigationToNoInternetFragment(){
        val action = MainNavigationDirections.actionGlobalNoInternetFragment()
        navigationController.navigate(action)
    }

    fun start(){
        navigationController.addOnDestinationChangedListener(destinationChangedListener)
        internetConnectionHandler.onUnavailable {
            navigationToNoInternetFragment()
        }
    }
    fun stop(){
        navigationController.removeOnDestinationChangedListener(destinationChangedListener)
    }

    init {
            navigationController.addOnDestinationChangedListener(destinationChangedListener)
            internetConnectionHandler.onUnavailable {
                navigationToNoInternetFragment()
            }
    }

}