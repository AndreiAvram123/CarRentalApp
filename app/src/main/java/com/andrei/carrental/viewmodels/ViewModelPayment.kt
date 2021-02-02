package com.andrei.carrental.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.andrei.engine.repository.PaymentRepository
import com.andrei.engine.requestModels.NewBookingRequestModel

class ViewModelPayment @ViewModelInject constructor(
    private val paymentRepository: PaymentRepository
): ViewModel(){


    val clientToken  by lazy{
        paymentRepository.fetchClientToken().asLiveData()
    }


    fun checkout(checkoutRequest: NewBookingRequestModel)= paymentRepository.checkout(checkoutRequest).asLiveData()
}