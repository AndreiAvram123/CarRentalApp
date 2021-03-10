package com.andrei.carrental.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.andrei.engine.State
import com.andrei.engine.repository.PaymentRepository
import com.andrei.engine.requestModels.NewBookingRequestModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelPayment @Inject constructor(
    private val paymentRepository: PaymentRepository
): ViewModel(){


    private val _clientToken :MutableStateFlow<State<String>> = MutableStateFlow(State.Loading)

    val clientToken:StateFlow<State<String>>
    get() = _clientToken

    fun clearToken(){
        viewModelScope.launch {
            _clientToken.emit(State.Loading)
        }
    }

    fun getClientToken(){
        viewModelScope.launch {
            paymentRepository.fetchClientToken().collect { state->
                when(state){
                    is State.Success -> _clientToken.emit(State.Success(state.data?.token))
                    is State.Error -> _clientToken.emit(State.Error(state.error))
                    else -> _clientToken.emit(State.Loading)
                }
            }
        }
    }

    fun checkout(checkoutRequest: NewBookingRequestModel)= paymentRepository.checkout(checkoutRequest).asLiveData()
}