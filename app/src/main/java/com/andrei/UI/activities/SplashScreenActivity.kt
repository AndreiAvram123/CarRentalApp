package com.andrei.UI.activities
import androidx.activity.viewModels
import com.andrei.UI.activities.LoginFlowActivity
import com.andrei.UI.activities.MainActivity
import com.andrei.carrental.R
import com.andrei.carrental.viewmodels.ViewModelAuth
import com.andrei.utils.reObserve
import com.andrei.utils.startNewActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {
    private val viewModelAuth:ViewModelAuth by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        viewModelAuth.authenticationState.reObserve(this){
            if(it == ViewModelAuth.AuthenticationState.NOT_AUTHENTICATED){
                startNewActivity<LoginFlowActivity>()
            }
            if(it == ViewModelAuth.AuthenticationState.AUTHENTICATED){
                startNewActivity<MainActivity>()
            }
        }
    }
}