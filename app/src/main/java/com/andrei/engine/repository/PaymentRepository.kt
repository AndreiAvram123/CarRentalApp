package com.andrei.engine.repository

import com.andrei.engine.CallRunner
import com.andrei.engine.DTOEntities.CheckoutRequest
import com.andrei.engine.configuration.AuthInterceptorWithToken
import com.andrei.engine.repositoryInterfaces.PaymentRepoInterface
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PaymentRepository {

    private val callRunner = CallRunner()
    private val retrofit = Retrofit.Builder().baseUrl("https://car-rental-api-kotlin.herokuapp.com").addConverterFactory(
        GsonConverterFactory.create()).client(OkHttpClient.Builder().addInterceptor(AuthInterceptorWithToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0VXNlcm5hbWUiLCJleHAiOjE2MTMwNzkwMTksInVzZXJJRCI6NCwidXNlcm5hbWUiOiJ0ZXN0VXNlcm5hbWUifQ.aeHTZ4WAGjD1h-zCTXZFSM-aN6cD-81f0UWA05EQ8xvYnO7TgKu2jPvaM2jbhLswM2HexhgNxi3BV1yinWFZJQ")).build()).build()
    private val  repo = retrofit.create(PaymentRepoInterface::class.java)

   fun fetchClientToken() = flow {
       callRunner.makeApiCall(repo.fetchClientToken()){
           emit(it)
       }
   }.flowOn(GlobalScope.coroutineContext)

fun checkout(checkoutRequest: CheckoutRequest) = flow{
    callRunner.makeApiCall(repo.checkout(checkoutRequest)){
        emit(it)
    }
}.flowOn(GlobalScope.coroutineContext)

}