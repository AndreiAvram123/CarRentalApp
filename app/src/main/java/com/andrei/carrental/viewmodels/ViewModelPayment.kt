package com.andrei.carrental.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.andrei.engine.DTOEntities.CheckoutRequest
import com.andrei.engine.repository.PaymentRepository

class ViewModelPayment @ViewModelInject constructor(
    private val paymentRepository: PaymentRepository
): ViewModel(){


    val clientToken  by lazy{
        paymentRepository.fetchClientToken().asLiveData()
    }

    fun checkout(checkoutRequest: CheckoutRequest)= paymentRepository.checkout(checkoutRequest).asLiveData()
}