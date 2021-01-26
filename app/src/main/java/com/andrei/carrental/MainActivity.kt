package com.andrei.carrental

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.andrei.carrental.databinding.ActivityMainBinding
import com.andrei.utils.LocationSettingsHandler
import com.andrei.utils.reObserve
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding

    private lateinit var locationSettingsHandler: LocationSettingsHandler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setUpNavigation()
        attachLocationObserver()
    }

    private fun attachLocationObserver() {
        locationSettingsHandler.currentLocationRequest.reObserve(this){
           locationSettingsHandler.requestChangeLocationForCurrentRequest(this@MainActivity)
        }
    }

    private fun setUpNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(LocationSettingsHandler.REQUEST_CHECK_SETTINGS == 2){
            if(resultCode == RESULT_OK){
               locationSettingsHandler.currentLocationNeedsSatisfied.value = true
            }
        }
    }
}