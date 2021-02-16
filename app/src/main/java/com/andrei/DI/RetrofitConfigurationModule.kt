package com.andrei.DI

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.andrei.DI.annotations.RetrofitInterceptorNoToken
import com.andrei.DI.annotations.RetrofitInterceptorWithToken
import com.andrei.carrental.R
import com.andrei.engine.configuration.AuthInterceptorNoToken
import com.andrei.engine.configuration.AuthInterceptorWithToken
import com.andrei.engine.helpers.TokenManager
import com.andrei.engine.repository.implementation.LoginRepositoryImpl
import com.andrei.engine.repository.interfaces.LoginRepository
import com.andrei.utils.getStringOrNull
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Duration

@Module
@InstallIn(ViewModelComponent::class)
class RetrofitConfigurationModule {


    @Provides
    fun provideInterceptorWithNoToken(): AuthInterceptorNoToken = AuthInterceptorNoToken()

    @Provides
    fun provideInterceptorWithToken(@ApplicationContext context: Context, sharedPreferences: SharedPreferences): AuthInterceptorWithToken {
        val token = sharedPreferences.getStringOrNull(context.getString(R.string.key_token))
        check(token !=null){"Token should not be null"}
         return AuthInterceptorWithToken(token)
    }

    @Provides
    fun provideRetrofitBuilderDefaultConfig():Retrofit.Builder{
        return Retrofit.Builder().baseUrl("https://car-rental-api-kotlin.herokuapp.com").addConverterFactory(
                GsonConverterFactory.create())
    }

    @RetrofitInterceptorNoToken
    @Provides
    fun provideRetrofitWithNoTokenInterceptor(authInterceptor: AuthInterceptorNoToken, builder:Retrofit.Builder): Retrofit {
        return builder.client(OkHttpClient.Builder().connectTimeout(Duration.ofSeconds(10)).addInterceptor(authInterceptor).build()).build()

    }

    @RetrofitInterceptorWithToken
    @Provides
    fun provideRetrofitWithTokenInterceptor(authInterceptorWithToken: AuthInterceptorWithToken, builder: Retrofit.Builder): Retrofit {
        return builder.client(OkHttpClient.Builder().connectTimeout(Duration.ofSeconds(10)).addInterceptor(authInterceptorWithToken).build()).build()
    }
}