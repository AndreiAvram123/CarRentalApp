package com.andrei.UI.activities
import androidx.activity.viewModels
import com.andrei.carrental.R
import com.andrei.carrental.viewmodels.ViewModelLogin
import com.andrei.utils.reObserve
import com.andrei.utils.startNewActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.andrei.engine.helpers.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {

    @Inject
     lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        lifecycleScope.launchWhenResumed {
            sessionManager.authenticationState.collect {
                if (it == SessionManager.AuthenticationState.NOT_AUTHENTICATED) {
                    startNewActivity<LoginFlowActivity>()
                }
                if (it == SessionManager.AuthenticationState.AUTHENTICATED) {
                    startNewActivity<MainActivity>()
                }
            }
        }
    }
}