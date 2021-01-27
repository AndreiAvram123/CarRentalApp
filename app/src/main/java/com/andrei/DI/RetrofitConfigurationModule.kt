package com.andrei.DI

import com.andrei.DI.annotations.RetrofitInterceptorNoToken
import com.andrei.DI.annotations.RetrofitInterceptorWithToken
import com.andrei.engine.configuration.AuthInterceptorNoToken
import com.andrei.engine.configuration.AuthInterceptorWithToken
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
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
    fun provideInterceptorWithToken(): AuthInterceptorWithToken = AuthInterceptorWithToken(token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0VXNlcm5hbWUiLCJleHAiOjE2MTMwNzkwMTksInVzZXJJRCI6NCwidXNlcm5hbWUiOiJ0ZXN0VXNlcm5hbWUifQ.aeHTZ4WAGjD1h-zCTXZFSM-aN6cD-81f0UWA05EQ8xvYnO7TgKu2jPvaM2jbhLswM2HexhgNxi3BV1yinWFZJQ")


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