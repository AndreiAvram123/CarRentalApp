package com.andrei.engine.configuration

import android.content.Context
import android.content.SharedPreferences
import com.andrei.carrental.R
import com.andrei.engine.repositoryInterfaces.ChatService
import com.andrei.utils.getStringOrNull
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Duration

class  APIImpl(private val sharedPreferences: SharedPreferences,
               private val context: Context,
               private val apiConfig: ApiConfig) {


    private fun provideRetrofitBuilder(apiConfig: ApiConfig):Retrofit.Builder{
        return Retrofit.Builder().baseUrl(apiConfig.baseURl).addConverterFactory(
            GsonConverterFactory.create())
    }


   private  fun provideInterceptorWithToken(context: Context,
                                     sharedPreferences: SharedPreferences): AuthInterceptorWithToken {
        val token = sharedPreferences.getStringOrNull(context.getString(R.string.key_token))
        check(token !=null){"Token should not be null"}
        return AuthInterceptorWithToken(token)
    }




    private fun provideHttpClientBuilder():OkHttpClient.Builder{
        return OkHttpClient.Builder().connectTimeout(Duration.ofSeconds(10))
    }

    private fun getRetrofitWithTokenInterceptor():Retrofit{
        val interceptor = provideInterceptorWithToken(context,sharedPreferences)
        return provideRetrofitBuilder(apiConfig)
            .client(provideHttpClientBuilder().addInterceptor(interceptor).build())
            .build()
    }

    val chatsService: ChatService = getRetrofitWithTokenInterceptor().create(ChatService::class.java)


}