package com.andrei.carrental.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.andrei.engine.DTOEntities.CheckoutRequest
import com.andrei.engine.State
import com.andrei.engine.repository.PaymentRepository

class ViewModelPayment : ViewModel(){

    private val repo = PaymentRepository()

    val clientToken  by lazy{
        repo.fetchClientToken().asLiveData()
    }

    fun makePayment(checkoutRequest: CheckoutRequest)= repo.makePayment(checkoutRequest).asLiveData()
}