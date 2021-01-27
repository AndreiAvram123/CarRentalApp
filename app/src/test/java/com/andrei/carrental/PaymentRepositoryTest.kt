package com.andrei.carrental

import android.os.Build
import com.andrei.engine.CallRunner
import com.andrei.engine.DTOEntities.CheckoutRequest
import com.andrei.engine.configuration.AuthInterceptorWithToken
import com.andrei.engine.repositoryInterfaces.PaymentRepoInterface
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Config(sdk = [Build.VERSION_CODES.O_MR1])
@RunWith(RobolectricTestRunner::class)
class PaymentRepositoryTest {
    private val callRunner = CallRunner()
    private val retrofit = Retrofit.Builder().baseUrl("https://car-rental-api-kotlin.herokuapp.com").addConverterFactory(
            GsonConverterFactory.create()).client(OkHttpClient.Builder().addInterceptor(AuthInterceptorWithToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0VXNlcm5hbWUiLCJleHAiOjE2MTMwNzkwMTksInVzZXJJRCI6NCwidXNlcm5hbWUiOiJ0ZXN0VXNlcm5hbWUifQ.aeHTZ4WAGjD1h-zCTXZFSM-aN6cD-81f0UWA05EQ8xvYnO7TgKu2jPvaM2jbhLswM2HexhgNxi3BV1yinWFZJQ")).build()).build()
    private val repo = retrofit.create(PaymentRepoInterface::class.java)

    @Test
    fun makePayment(){
        runBlocking {
            callRunner.makeApiCall(repo.checkout(CheckoutRequest(amount = 1011,nonce = "fake-valid-nonce",deviceData = null))){
               print(it)
            }
        }
    }


}