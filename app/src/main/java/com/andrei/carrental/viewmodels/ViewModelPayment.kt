package com.andrei.carrental.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.andrei.engine.DTOEntities.CheckoutRequest
import com.andrei.engine.repository.PaymentRepository

class ViewModelPayment : ViewModel(){

    private val repo = PaymentRepository()

    val clientToken  by lazy{
        repo.fetchClientToken().asLiveData()
    }

    fun checkout(checkoutRequest: CheckoutRequest)= repo.checkout(checkoutRequest).asLiveData()
}