package com.andrei.DI

import android.content.Context
import com.andrei.carrental.R
import com.andrei.engine.helpers.TokenManager
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
    fun provideSharePrefs(@ApplicationContext context: Context) =    context.getSharedPreferences(context.getString(R.string.key_preferences), Context.MODE_PRIVATE)

}