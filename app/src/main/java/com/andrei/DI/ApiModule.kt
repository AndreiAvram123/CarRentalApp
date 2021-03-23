package com.andrei.DI

import com.andrei.DI.annotations.RetrofitInterceptorNoToken
import com.andrei.DI.annotations.RetrofitInterceptorWithToken
import com.andrei.engine.APIs.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
class ApiModule {

    @Provides
    fun provideAuthRepoInterface(
            @RetrofitInterceptorNoToken
            retrofit: Retrofit): LoginAPI = retrofit.create(LoginAPI::class.java)
    @Provides
    fun provideSignUphRepoInterface(
            @RetrofitInterceptorNoToken
            retrofit: Retrofit): SignUpAPI = retrofit.create(SignUpAPI::class.java)


    @Provides
    fun provideCarRarRepoInterface(
            @RetrofitInterceptorWithToken retrofit: Retrofit
    ):CarAPI = retrofit.create(CarAPI::class.java)


    @Provides
    fun providePaymentRepoInterface(
        @RetrofitInterceptorWithToken retrofit: Retrofit
    ): PaymentRepoInterface = retrofit.create(PaymentRepoInterface::class.java)

    @Provides
    fun provideUserRepoInterface(
            @RetrofitInterceptorWithToken retrofit: Retrofit
    ):UserRepoInterface = retrofit.create(UserRepoInterface::class.java)

    @Provides
    fun provideBookingsRepoInterface(
            @RetrofitInterceptorWithToken retrofit: Retrofit
    ):BookingRepoInterface = retrofit.create(BookingRepoInterface::class.java)

    @Provides
    fun provideUserAPI(
        @RetrofitInterceptorWithToken retrofit: Retrofit
    ):UserAPI = retrofit.create(UserAPI::class.java)

    @Provides
    fun provideChatAPI(@RetrofitInterceptorWithToken retrofit: Retrofit):ChatAPI = retrofit.create(ChatAPI::class.java)

}