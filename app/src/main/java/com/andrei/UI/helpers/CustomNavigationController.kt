package com.andrei.UI.helpers


import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.andrei.carrental.MainNavigationDirections
import com.andrei.carrental.R
import com.andrei.utils.gone
import com.andrei.utils.show
import com.google.android.material.bottomnavigation.BottomNavigationView

class CustomNavigationController(private val navigationController: NavController,
                                private val  internetConnectionHandler: InternetConnectionHandler,
                                private val bottomNavigationView: BottomNavigationView) {

    private val destinationsWithoutBottomNav = listOf(R.id.noInternetFragment, R.id.redeemVoucherFragment)


    private val destinationChangedListener:NavController.OnDestinationChangedListener =
            NavController.OnDestinationChangedListener { _, destination, _ ->
                toggleNavBar(destination)
                shouldNavigateToNoInternetFragment(destination)
            }


    private fun shouldNavigateToNoInternetFragment(destination: NavDestination) {
        if (internetConnectionHandler.isNotConnected()) {
            if (destination.id != R.id.noInternetFragment) {
                navigationToNoInternetFragment()
            }
        }
    }

    private fun toggleNavBar(destination: NavDestination) {
        bottomNavigationView.show()
        destinationsWithoutBottomNav.find { it == destination.id }?.let {
            bottomNavigationView.gone()
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