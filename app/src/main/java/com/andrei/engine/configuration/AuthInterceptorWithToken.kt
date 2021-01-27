package com.andrei.engine.configuration

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptorWithToken(private val token: String? = null,  private val recheckToken :() -> Unit
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val requestBuilder = chain.request().newBuilder()

        recheckToken()

        token?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }


        return chain.proceed(requestBuilder.build())
    }
}