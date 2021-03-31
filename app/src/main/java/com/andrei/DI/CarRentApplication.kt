package com.andrei.DI

import android.app.Application
import com.andrei.carrental.BuildConfig
import com.andrei.carrental.custom.RemoteConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class CarRentApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }
    }
}