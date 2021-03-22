package com.andrei.engine.repository

import com.andrei.carrental.UserDataManager
import com.andrei.carrental.entities.CheckoutCarData
import com.andrei.engine.CallRunner
import com.andrei.engine.State
import com.andrei.engine.repositoryInterfaces.PaymentRepoInterface
import com.andrei.engine.requestModels.NewBookingRequestModel
import com.andrei.engine.requestModels.PaymentRequest
import com.andrei.engine.responseModels.TokenResponse
import com.andrei.utils.toUnix
import com.braintreepayments.api.dropin.DropInResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PaymentRepository @Inject constructor(
    private val paymentRepoInterface: PaymentRepoInterface,
    private val callRunner: CallRunner,
    private val userDataManager: UserDataManager
){

   fun fetchClientToken():Flow<State<TokenResponse>> =
       callRunner.makeApiCall{this@PaymentRepository.paymentRepoInterface.fetchClientToken() }


 fun checkout(checkoutRequest: Pair<CheckoutCarData,DropInResult>):Flow<State<Nothing>> {
     val userID:Long = userDataManager.userID!!

     val dropInResult = checkoutRequest.second
     val carCheckoutData = checkoutRequest.first
     val paymentRequest = PaymentRequest (
             nonce = dropInResult.paymentMethodNonce?.nonce,
             deviceData = dropInResult.deviceData,
             amount = carCheckoutData.amount)

     val newBookingRequestModel = NewBookingRequestModel(
         paymentRequest = paymentRequest,
         startDate = carCheckoutData.bookingDate.startDate.toUnix(),
         endDate = carCheckoutData.bookingDate.endDate.toUnix(),
         carID = carCheckoutData.carID,
         userID = userID
     )
     return callRunner.makeApiCall {
         paymentRepoInterface.checkout(newBookingRequestModel)
     }
}

}