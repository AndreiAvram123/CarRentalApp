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
import javax.inject.Inject

class PaymentRepository @Inject constructor(
    private val paymentRepoInterface: PaymentRepoInterface,
    private val callRunner: CallRunner
){

   fun fetchClientToken() = flow {
       callRunner.makeApiCall(this@PaymentRepository.paymentRepoInterface.fetchClientToken()){
           emit(it)
       }
   }.flowOn(GlobalScope.coroutineContext)

fun checkout(checkoutRequest: CheckoutRequest) = flow{
    callRunner.makeApiCall(this@PaymentRepository.paymentRepoInterface.checkout(checkoutRequest)){
        emit(it)
    }
}.flowOn(GlobalScope.coroutineContext)

}