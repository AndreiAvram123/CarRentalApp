package com.andrei.carrental.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.andrei.engine.repository.PaymentRepository
import com.andrei.engine.requestModels.NewBookingRequestModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ViewModelPayment @Inject constructor(
    private val paymentRepository: PaymentRepository
): ViewModel(){


    val clientToken  by lazy{
        paymentRepository.fetchClientToken().asLiveData()
    }


    fun checkout(checkoutRequest: NewBookingRequestModel)= paymentRepository.checkout(checkoutRequest).asLiveData()
}