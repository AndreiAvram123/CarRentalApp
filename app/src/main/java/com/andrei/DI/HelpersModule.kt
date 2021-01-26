package com.andrei.DI

import com.andrei.utils.LocationSettingsHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Singleton

@InstallIn(ActivityComponent::class)
@Module

class HelpersModule {

    @Provides
    @ActivityScoped
    fun provideLocationsSettingsHandler()  = LocationSettingsHandler()



}