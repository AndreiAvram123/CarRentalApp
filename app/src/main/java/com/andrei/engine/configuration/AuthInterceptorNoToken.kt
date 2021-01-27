package com.andrei.engine.configuration

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val token: String? = null
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val requestBuilder = chain.request().newBuilder()

        token?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }


        return chain.proceed(requestBuilder.build())
    }
}