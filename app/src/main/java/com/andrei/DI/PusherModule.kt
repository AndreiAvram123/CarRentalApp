package com.andrei.DI

import android.content.Context
import android.content.SharedPreferences
import com.andrei.carrental.R
import com.andrei.carrental.room.dao.MessageDao
import com.andrei.messenger.MessengerService
import com.andrei.messenger.PusherConfig
import com.andrei.utils.getStringOrNull
import com.pusher.client.PusherOptions
import com.pusher.client.util.HttpAuthorizer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped

@InstallIn(ActivityComponent::class)
@Module

class PusherModule {

    @ActivityScoped
    @Provides
    fun provideHttpAuthorizer(sharedPreferences: SharedPreferences, @ApplicationContext context: Context):HttpAuthorizer {
        val authorizer = HttpAuthorizer(context.getString(R.string.pusher_auth_endpoint))
        val token = sharedPreferences.getStringOrNull(context.getString(R.string.key_token))
        check(token !=null){"Token should not be null"}

        val headers = mapOf("Authorization" to "Bearer $token")
        authorizer.setHeaders(headers)
        return authorizer
    }
    @ActivityScoped
    @Provides
    fun providePusherOptions( @ApplicationContext context: Context, authorizer: HttpAuthorizer): PusherOptions {
        val options = PusherOptions()

        options.apply {
            setCluster(context.getString(R.string.pusher_app_cluster))
            setAuthorizer(authorizer)
        }
        return options
    }

    @Provides
    @ActivityScoped
    fun providePusherConfig(@ApplicationContext context: Context, pusherOptions: PusherOptions): PusherConfig {
        return PusherConfig(pusherOptions = pusherOptions, pusherKey = context.getString(R.string.pusher_key))
    }

}