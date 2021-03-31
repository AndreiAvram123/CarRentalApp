package com.andrei.UI.helpers


import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.andrei.carrental.MainNavigationDirections
import com.andrei.carrental.R
import com.andrei.utils.gone
import com.andrei.utils.show
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CustomNavigationController(private val navigationController: NavController,
                                private val  internetConnectionHandler: InternetConnectionHandler,
                                private val bottomNavigationView: BottomNavigationView) {

    private val destinationsWithoutBottomNav = listOf(R.id.noInternetFragment, R.id.redeemVoucherFragment)


    private val destinationChangedListener:NavController.OnDestinationChangedListener =
            NavController.OnDestinationChangedListener { _, destination, _ ->
                toggleNavBar(destination)

            }


    private fun toggleNavBar(destination: NavDestination) {
        bottomNavigationView.show()
        destinationsWithoutBottomNav.find { it == destination.id }?.let {
            bottomNavigationView.gone()
        }
    }


    private fun navigationToNoInternetFragment(){
        GlobalScope.launch(Dispatchers.Main) {
            val action = MainNavigationDirections.actionGlobalNoInternetFragment()
            navigationController.navigate(action)
        }
    }

    init {
        GlobalScope.launch(Dispatchers.Default) {
              internetConnectionHandler.isConnectedState.collect{connected->
                  if(connected){
                      if(isNoInternetFragmentDisplayed()){
                         popLastDestination()
                      }
                  }else{
                      if(!isNoInternetFragmentDisplayed()){
                         navigationToNoInternetFragment()
                      }
                  }
              }
        }
    }

    fun start(){
        navigationController.addOnDestinationChangedListener(destinationChangedListener)

    }

    private fun popLastDestination() {
       GlobalScope.launch(Dispatchers.Main) {
           navigationController.popBackStack()
        }
    }

    fun stop(){
        navigationController.removeOnDestinationChangedListener(destinationChangedListener)
    }

    private fun isNoInternetFragmentDisplayed(): Boolean{
        val currentBackStackEntry = navigationController.currentBackStackEntry
        if(currentBackStackEntry != null){
            return currentBackStackEntry.destination.id == R.id.noInternetFragment
        }
        return false
    }

}