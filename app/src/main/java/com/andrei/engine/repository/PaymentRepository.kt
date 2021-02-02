package com.andrei.engine.repository

import com.andrei.engine.CallRunner
import com.andrei.engine.repositoryInterfaces.PaymentRepoInterface
import com.andrei.engine.requestModels.NewBookingRequestModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
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

fun checkout(checkoutRequest: NewBookingRequestModel) = flow{
    callRunner.makeApiCall(this@PaymentRepository.paymentRepoInterface.checkout(checkoutRequest)){
        emit(it)
    }
}.flowOn(GlobalScope.coroutineContext)

}