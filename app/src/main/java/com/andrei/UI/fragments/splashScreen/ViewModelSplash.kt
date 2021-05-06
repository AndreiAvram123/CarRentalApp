package com.andrei.UI.fragments.splashScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrei.carrental.BuildConfig
import com.andrei.engine.helpers.SessionManager
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@HiltViewModel
class ViewModelSplash constructor(
    private val sessionManager: SessionManager
): ViewModel() {

    private val _stateFlow:MutableStateFlow<SplashState> = MutableStateFlow(SplashState.Loading)

    val stateFlow:StateFlow<SplashState>
    get() = _stateFlow.asStateFlow()

    init {
        viewModelScope.launch {
            delay(2000)
            activateRemoteConfig()
        }
    }

    private suspend fun activateRemoteConfig(){
        provideRemoteConfigSettings()
        try{
            val remoteConfig = Firebase.remoteConfig
            remoteConfig.fetchAndActivate().await()
            checkAppVersion()
        }catch (e:Exception){
            listenToAuthenticationState()
        }
    }

    private suspend fun checkAppVersion() {
        val currentAppVersion = BuildConfig.VERSION_CODE
        if(Firebase.remoteConfig.getLong("app_version_code") > currentAppVersion){
            _stateFlow.emit(SplashState.NavigateToUpdateApp)
        }else{
            listenToAuthenticationState()
        }
    }


    private fun listenToAuthenticationState() {
        viewModelScope.launch {
            sessionManager.authenticationState.collect { state ->
                when (state) {
                    SessionManager.AuthenticationState.AUTHENTICATED -> {
                        _stateFlow.emit(SplashState.NavigateToHome)
                    }
                    SessionManager.AuthenticationState.NOT_AUTHENTICATED -> {
                        _stateFlow.emit(SplashState.NavigateToLogin)
                    }
                }
            }
        }
    }
    }

    private fun provideRemoteConfigSettings(){
        if(BuildConfig.DEBUG) {
            val settings = remoteConfigSettings {
                minimumFetchIntervalInSeconds = 3600
            }
            Firebase.remoteConfig.setConfigSettingsAsync(settings)
        }
    }

    sealed class SplashState{
        object NavigateToLogin: SplashState()
        object NavigateToHome:SplashState()
        object NavigateToUpdateApp:SplashState()
        object Loading:SplashState()
    }
}