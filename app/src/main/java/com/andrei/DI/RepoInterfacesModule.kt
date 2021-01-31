package com.andrei.DI

import com.andrei.DI.annotations.RetrofitInterceptorNoToken
import com.andrei.DI.annotations.RetrofitInterceptorWithToken
import com.andrei.engine.configuration.AuthInterceptorWithToken
import com.andrei.engine.configuration.AuthInterceptorNoToken
import com.andrei.engine.repository.interfaces.SignUpRepository
import com.andrei.engine.repository.interfaces.SignUpRepositoryInterface
import com.andrei.engine.repositoryInterfaces.AuthRepoInterface
import com.andrei.engine.repositoryInterfaces.CarRepoInterface
import com.andrei.engine.repositoryInterfaces.PaymentRepoInterface
import com.cardinalcommerce.cardinalmobilesdk.models.Payment
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.time.Duration

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
            retrofit: Retrofit): SignUpRepositoryInterface = retrofit.create(SignUpRepositoryInterface::class.java)


    @Provides
    fun provideCarRarRepoInterface(
            @RetrofitInterceptorWithToken retrofit: Retrofit
    ):CarRepoInterface = retrofit.create(CarRepoInterface::class.java)


    @Provides
    fun providePaymentRepoInterface(
        @RetrofitInterceptorWithToken retrofit: Retrofit
    ): PaymentRepoInterface = retrofit.create(PaymentRepoInterface::class.java)

}