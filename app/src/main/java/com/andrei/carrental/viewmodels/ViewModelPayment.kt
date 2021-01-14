package com.andrei.carrental.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.andrei.engine.State
import com.andrei.engine.repository.PaymentRepository

class ViewModelPayment : ViewModel(){

    private val repo = PaymentRepository()

    val clientToken  by lazy{
        repo.fetchClientToken().asLiveData()
    }
}