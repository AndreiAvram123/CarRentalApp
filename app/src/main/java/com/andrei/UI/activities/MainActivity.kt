package com.andrei.UI.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.andrei.UI.helpers.InternetConnectionHandler
import com.andrei.carrental.R
import com.andrei.carrental.databinding.ActivityMainBinding
import com.andrei.carrental.viewmodels.ViewModelAuth
import com.andrei.engine.helpers.UserManager
import com.andrei.engine.states.LoginFlowState
import com.andrei.utils.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    private lateinit var binding:ActivityMainBinding
    private lateinit var navController:NavController
    private val  viewModelAuth:ViewModelAuth by viewModels()
    private var internetConnectionHandler:InternetConnectionHandler? = null


    private val observerUserLoggedIn = Observer<ViewModelAuth.AuthenticationState>{
        if(it ==  ViewModelAuth.AuthenticationState.NOT_AUTHENTICATED){
            startNewActivity<LandingActivity>()
        }
    }


    @Inject
      lateinit var locationSettingsHandler: LocationSettingsHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setUpNavigation()
        attachLocationObserver()
        viewModelAuth.authenticationState.reObserve(this,observerUserLoggedIn)

    }

    private fun startInternetConnectionHandler() {
        internetConnectionHandler = InternetConnectionHandler(navController, this.getConnectivityManager())
    }

    private fun attachLocationObserver() {
        locationSettingsHandler.registerActivityForResult(this)
    }

    private fun setUpNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(LocationSettingsHandler.REQUEST_CHECK_SETTINGS == requestCode){
            if(resultCode.isResultOk()){
               locationSettingsHandler.currentLocationNeedsSatisfied.value = true
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
}