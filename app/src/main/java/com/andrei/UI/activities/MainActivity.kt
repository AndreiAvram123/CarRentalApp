package com.andrei.UI.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import by.kirich1409.viewbindingdelegate.viewBinding
import com.andrei.UI.helpers.InternetConnectionHandler
import com.andrei.carrental.R
import com.andrei.carrental.databinding.ActivityMainBinding
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
    private var internetConnectionHandler: InternetConnectionHandler? = null


    @Inject
    lateinit var messengerService: MessengerService

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
    }


    private fun startInternetConnectionHandler() {
        internetConnectionHandler =
            InternetConnectionHandler(navController, this.getConnectivityManager())
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
        startInternetConnectionHandler()
    }

    override fun onStop() {
        super.onStop()
        internetConnectionHandler = null
    }

    override fun onDestroy() {
        super.onDestroy()
        messengerService.disconnect()
    }
}