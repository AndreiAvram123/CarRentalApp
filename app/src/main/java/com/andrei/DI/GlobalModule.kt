package com.andrei.DI

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import com.andrei.carrental.R
import com.andrei.engine.helpers.TokenManager
import com.andrei.utils.getConnectivityManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module

class GlobalModule {

    @Singleton
    @Provides
    fun provideSharePrefs(@ApplicationContext context: Context): SharedPreferences =    context.getSharedPreferences(context.getString(R.string.key_preferences), Context.MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideNetworkManager(@ApplicationContext context:Context) : ConnectivityManager = context.getConnectivityManager()

}