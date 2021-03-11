package com.andrei.DI

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import com.andrei.carrental.R
import com.andrei.engine.helpers.TokenManager
import com.andrei.utils.getConnectivityManager
import com.andrei.utils.getStringOrNull
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.DefineComponent
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import pl.aprilapps.easyphotopicker.EasyImage
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

class GlobalModule {

    @Singleton
    @Provides
    fun provideSharePrefs(@ApplicationContext context: Context): SharedPreferences =    context.getSharedPreferences(context.getString(R.string.key_preferences), Context.MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideNetworkManager(@ApplicationContext context:Context) : ConnectivityManager = context.getConnectivityManager()

    @Singleton
    @Provides
    fun provideEasyImage(@ApplicationContext context: Context):EasyImage = EasyImage.Builder(context).setFolderName("Car Rental").allowMultiple(true).build()

}