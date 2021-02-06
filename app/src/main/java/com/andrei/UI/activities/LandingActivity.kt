package com.andrei.UI.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.andrei.carrental.R
import com.andrei.carrental.viewmodels.ViewModelAuth
import com.andrei.engine.helpers.UserManager
import com.andrei.utils.reObserve
import com.andrei.utils.startNewActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LandingActivity : AppCompatActivity() {

    private val viewModelAuth:ViewModelAuth by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)
        viewModelAuth.authenticationState.reObserve(this){
            if(it == ViewModelAuth.AuthenticationState.AUTHENTICATED){
                startNewActivity<MainActivity>()
            }
        }

    }
}