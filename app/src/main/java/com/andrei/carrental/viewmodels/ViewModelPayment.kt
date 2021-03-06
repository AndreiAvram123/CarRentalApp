package com.andrei.carrental.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrei.carrental.entities.CheckoutCarData
import com.andrei.engine.State
import com.andrei.engine.repository.PaymentRepository
import com.braintreepayments.api.dropin.DropInResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelPayment @Inject constructor(
    private val paymentRepository: PaymentRepository
): ViewModel(){


    private val _clientToken :MutableStateFlow<State<String>> = MutableStateFlow(State.Loading)
    val clientToken:StateFlow<State<String>>
    get() = _clientToken.asStateFlow()

     fun clearState(){
        viewModelScope.launch {
            _clientToken.emit(State.Loading)
            _dropInRequest.emit(null)
            _checkoutCarData.emit(null)
        }
    }


    private  val _checkoutState:MutableStateFlow<State<Nothing>> = MutableStateFlow(State.Default)
    val checkoutState:StateFlow<State<Nothing>>
    get() = _checkoutState.asStateFlow()


    private val _checkoutCarData:MutableStateFlow<CheckoutCarData?> = MutableStateFlow(null)


    private val _dropInRequest:MutableStateFlow<DropInResult?>  = MutableStateFlow(null)



    fun startCheckout() =
        viewModelScope.launch {
            combine(
                    _checkoutCarData.filterNotNull(),
                    _dropInRequest.filterNotNull()
            ) { carData, dropInData ->
                Pair(carData, dropInData)
            }.collect{
                paymentRepository.checkout(it).collect {state->
                    _checkoutState.emit(state)
                }
        }
    }

     fun getClientToken(){
        viewModelScope.launch {
            paymentRepository.fetchClientToken().collect { state->
                when(state){
                    is State.Success -> _clientToken.emit(State.Success(state.data.token))
                    is State.Error -> _clientToken.emit(State.Error(state.error))
                    else -> _clientToken.emit(State.Loading)
                }
            }
        }
    }



    fun setDropInResult(dropInResult: DropInResult){
      viewModelScope.launch {
          _dropInRequest.emit(dropInResult)
      }
    }

    fun setCheckoutData(checkoutCarData: CheckoutCarData){
        viewModelScope.launch {
            _checkoutCarData.emit(checkoutCarData)
        }
    }


}