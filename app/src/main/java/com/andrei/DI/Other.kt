package com.andrei.DI

import com.andrei.utils.LocationSettingsHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
class Other {
    @Provides
    fun provideLocationsSettingsHandler() = LocationSettingsHandler()

}