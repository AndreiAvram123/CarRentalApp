package com.andrei.DI.annotations

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RetrofitInterceptorNoToken

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RetrofitInterceptorWithToken

