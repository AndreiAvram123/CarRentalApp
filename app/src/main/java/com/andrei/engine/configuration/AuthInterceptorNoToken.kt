package com.andrei.engine.configuration

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptorNoToken : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val requestBuilder = chain.request().newBuilder()




        return chain.proceed(requestBuilder.build())
    }
}