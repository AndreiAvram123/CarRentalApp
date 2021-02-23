package com.andrei.DI.modules

import com.andrei.DI.annotations.RetrofitInterceptorNoToken
import com.andrei.DI.annotations.RetrofitInterceptorWithToken
import com.andrei.engine.configuration.APIImpl
import com.andrei.engine.configuration.ApiConfig
import com.andrei.engine.repositoryInterfaces.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import org.koin.dsl.module
import retrofit2.Retrofit


val apiModule = module {
    single { ApiConfig() }
    single { APIImpl(get(),get(),get()) }
}

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
    fun provideChatAPI(@RetrofitInterceptorWithToken retrofit: Retrofit):ChatService = retrofit.create(ChatService::class.java)

}