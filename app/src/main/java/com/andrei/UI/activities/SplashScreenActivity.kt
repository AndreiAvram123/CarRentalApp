package com.andrei.UI.activities
import androidx.activity.viewModels
import com.andrei.carrental.R
import com.andrei.carrental.viewmodels.ViewModelLogin
import com.andrei.utils.reObserve
import com.andrei.utils.startNewActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {
    private val viewModelLogin:ViewModelLogin by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        viewModelLogin.authenticationState.reObserve(this){
            if(it == ViewModelLogin.AuthenticationState.NOT_AUTHENTICATED){
                startNewActivity<LoginFlowActivity>()
            }
            if(it == ViewModelLogin.AuthenticationState.AUTHENTICATED){
                startNewActivity<MainActivity>()
            }
        }
    }
}