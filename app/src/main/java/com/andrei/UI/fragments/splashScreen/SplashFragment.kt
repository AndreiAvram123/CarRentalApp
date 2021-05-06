package com.andrei.UI.fragments.splashScreen

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.andrei.UI.activities.MainActivity
import com.andrei.UI.fragments.BaseFragment
import com.andrei.carrental.R
import com.andrei.utils.startNewActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import com.andrei.UI.fragments.splashScreen.ViewModelSplash.SplashState

@AndroidEntryPoint
class SplashFragment : BaseFragment(R.layout.fragment_splash) {

    private val viewModelSplash:ViewModelSplash by viewModels()

    override fun initializeUI() {
      lifecycleScope.launchWhenResumed {
          viewModelSplash.stateFlow.collect {
              when(it){
                  SplashState.Loading -> {

                  }
                  SplashState.NavigateToHome -> {
                    navigateHome()
                  }
                  SplashState.NavigateToLogin -> {
                    navigateToLogin()
                  }
                  SplashState.NavigateToUpdateApp -> {

                  }
              }
          }
      }
    }

    private fun navigateHome() {
        startNewActivity<MainActivity>()
    }
    private fun navigateToLogin(){
        findNavController().navigate(
            SplashFragmentDirections.actionSplashFragmentToLoginFragment()
        )
    }
}