package com.andrei.engine.repository

import com.andrei.engine.CallRunner
import com.andrei.engine.State
import com.andrei.engine.repositoryInterfaces.PaymentRepoInterface
import com.andrei.engine.requestModels.NewBookingRequestModel
import com.andrei.engine.responseModels.TokenResponse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class PaymentRepository @Inject constructor(
    private val paymentRepoInterface: PaymentRepoInterface,
    private val callRunner: CallRunner
){

   fun fetchClientToken():Flow<State<TokenResponse>> =
       callRunner.makeApiCall{this@PaymentRepository.paymentRepoInterface.fetchClientToken() }


fun checkout(checkoutRequest: NewBookingRequestModel) =
    callRunner.makeApiCall{this@PaymentRepository.paymentRepoInterface.checkout(checkoutRequest) }

}