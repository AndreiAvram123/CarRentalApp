package com.andrei.DI

import android.content.Context
import android.content.SharedPreferences
import com.andrei.DI.annotations.RetrofitInterceptorNoToken
import com.andrei.DI.annotations.RetrofitInterceptorWithToken
import com.andrei.carrental.R
import com.andrei.engine.configuration.AuthInterceptorNoToken
import com.andrei.engine.configuration.AuthInterceptorWithToken
import com.andrei.engine.helpers.TokenManager
import com.andrei.utils.getStringOrNull
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Duration

@Module
@InstallIn(ActivityComponent::class)
class RetrofitConfigurationModule {

    @Provides
    fun provideInterceptorWithNoToken(): AuthInterceptorNoToken = AuthInterceptorNoToken()

    @Provides
    fun provideInterceptorWithToken(@ApplicationContext context: Context, sharedPreferences: SharedPreferences, tokenManager: TokenManager): AuthInterceptorWithToken {
        val token = sharedPreferences.getStringOrNull(context.getString(R.string.key_token))
         return AuthInterceptorWithToken(token,tokenManager::recheckToken)
    }

    @RetrofitInterceptorNoToken
    @Provides
    fun provideRetrofitWithNoTokenInterceptor(authInterceptor: AuthInterceptorNoToken): Retrofit {
        return Retrofit.Builder().baseUrl("https://car-rental-api-kotlin.herokuapp.com").addConverterFactory(
                GsonConverterFactory.create()).client(OkHttpClient.Builder().connectTimeout(Duration.ofSeconds(10)).addInterceptor(authInterceptor).build()).build()

    }

    @RetrofitInterceptorWithToken
    @Provides
    fun provideRetrofitWithTokenInterceptor(authInterceptorWithToken: AuthInterceptorWithToken): Retrofit {
        return Retrofit.Builder().baseUrl("https://car-rental-api-kotlin.herokuapp.com").addConverterFactory(
                GsonConverterFactory.create()).client(OkHttpClient.Builder().connectTimeout(Duration.ofSeconds(10)).addInterceptor(authInterceptorWithToken).build()).build()
    }
}