package com.andrei.UI.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import by.kirich1409.viewbindingdelegate.viewBinding
import com.andrei.UI.fragments.HomeFragmentDirections
import com.andrei.UI.helpers.CustomNavigationController
import com.andrei.UI.helpers.InternetConnectionHandler
import com.andrei.carrental.MainNavigationDirections
import com.andrei.carrental.R
import com.andrei.carrental.UserDataManager
import com.andrei.carrental.databinding.ActivityMainBinding
import com.andrei.carrental.viewmodels.ViewModelVoucher
import com.andrei.engine.State
import com.andrei.engine.helpers.SessionManager
import com.andrei.messenger.MessengerService
import com.andrei.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {


    private val binding: ActivityMainBinding by viewBinding()
    private lateinit var navController: NavController
    private val viewModelVouchers:ViewModelVoucher by viewModels()

    @Inject
    lateinit var userDataManager: UserDataManager

    @Inject
    lateinit var internetConnectionHandler: InternetConnectionHandler

    @Inject
    lateinit var messengerService: MessengerService

    private val customNavigationController:CustomNavigationController by lazy {
         CustomNavigationController(lifecycleScope,navController,internetConnectionHandler,binding.bottomNavigationView)
    }


    @Inject
    lateinit var sessionManager: SessionManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpNavigation()
        attachObservers()
    }

    private fun attachObservers() {
        lifecycleScope.launchWhenResumed {
            sessionManager.authenticationState.collect {
                if (it == SessionManager.AuthenticationState.NOT_AUTHENTICATED) {
                    startNewActivity<LoginFlowActivity>()
                }
            }
        }
        viewModelVouchers.getVouchers(userDataManager.userID)
        lifecycleScope.launchWhenResumed {
            viewModelVouchers.availableVouchers.collect {
                if(it is State.Success && it.data.isNotEmpty()){
                      navigateToVouchersFragment()
                }
            }
        }

    }


     private fun navigateToVouchersFragment(){
         val action = MainNavigationDirections.actionGlobalRedeemVoucherFragment()
         navController.navigate(action)
     }



    private fun setUpNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (LocationSettingsHandler.REQUEST_CHECK_SETTINGS == requestCode) {
            if (resultCode.isResultOk()) {
                LocationSettingsHandler.currentLocationNeedsSatisfied.value = true
            }
        }
    }

    override fun onStart() {
        super.onStart()
         customNavigationController.start()
         internetConnectionHandler.start()
    }

    override fun onStop() {
        super.onStop()
        customNavigationController.stop()
    }
}