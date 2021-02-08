package com.andrei.DI

import com.andrei.DI.annotations.RetrofitInterceptorNoToken
import com.andrei.DI.annotations.RetrofitInterceptorWithToken
import com.andrei.engine.repositoryInterfaces.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import retrofit2.Retrofit

@Module
@InstallIn(ActivityComponent::class)
class RepoInterfacesModule {

    @Provides
    fun provideAuthRepoInterface(
            @RetrofitInterceptorNoToken
            retrofit: Retrofit): AuthRepoInterface = retrofit.create(AuthRepoInterface::class.java)
    @Provides
    fun provideSignUphRepoInterface(
            @RetrofitInterceptorNoToken
            retrofit: Retrofit): SignUpAPI = retrofit.create(SignUpAPI::class.java)


    @Provides
    fun provideCarRarRepoInterface(
            @RetrofitInterceptorWithToken retrofit: Retrofit
    ):CarRepoInterface = retrofit.create(CarRepoInterface::class.java)


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

}