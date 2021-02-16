package com.andrei.DI

import android.content.Context
import android.content.SharedPreferences
import com.andrei.carrental.R
import com.andrei.utils.LocationSettingsHandler
import com.andrei.utils.getStringOrNull
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import com.pusher.client.util.HttpAuthorizer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module

class HelpersModule {


    @Provides
    fun provideHttpAuthorizer(sharedPreferences: SharedPreferences, @ApplicationContext context: Context):HttpAuthorizer {
        val authorizer = HttpAuthorizer(context.getString(R.string.pusher_auth_endpoint))
        val token = sharedPreferences.getStringOrNull(context.getString(R.string.key_token))
        check(token !=null){"Token should not be null"}

        val headers = mapOf("Authorization" to "Bearer $token")
        authorizer.setHeaders(headers)
        return authorizer
    }

    @Provides
    fun providePusher( @ApplicationContext context: Context, authorizer: HttpAuthorizer): Pusher {
        val options = PusherOptions()

        options.apply {
            setCluster(context.getString(R.string.pusher_app_cluster))
            setAuthorizer(authorizer)
        }
        return Pusher(
            context.getString(
                R.string.pusher_key
            ),options)
    }
}