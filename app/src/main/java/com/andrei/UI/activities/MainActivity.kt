package com.andrei.UI.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.andrei.carrental.R
import com.andrei.carrental.databinding.ActivityMainBinding
import com.andrei.carrental.viewmodels.ViewModelAuth
import com.andrei.utils.LocationSettingsHandler
import com.andrei.utils.isResultOk
import com.andrei.utils.reObserve
import com.andrei.utils.startNewActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding
    private val  viewModelAuth:ViewModelAuth by viewModels()

    private val observerUserLoggedIn = Observer<Boolean>{
        if(it == false){
            startNewActivity<LandingActivity>()
        }
    }

    private fun startMainActivity() {
      startNewActivity<MainActivity>()
    }

    @Inject
      lateinit var locationSettingsHandler: LocationSettingsHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        attachLocationObserver()
        viewModelAuth.isUserLoggedIn.reObserve(this,observerUserLoggedIn)
        setUpNavigation()
    }

    private fun attachLocationObserver() {
        locationSettingsHandler.registerActivityForResult(this)
    }

    private fun setUpNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
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
}