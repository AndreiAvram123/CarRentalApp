package com.andrei.carrental.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrei.carrental.entities.Voucher
import com.andrei.engine.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelVoucher @Inject constructor() :ViewModel() {

    private val _availableVouchers:MutableStateFlow<State<List<Voucher>>> = MutableStateFlow(State.Loading)

    val availableVouchers:StateFlow<State<List<Voucher>>>
    get() = _availableVouchers.asStateFlow()


    fun getVouchers(userID:Long){
        viewModelScope.launch {
            _availableVouchers.emit(State.Success(listOf(Voucher(1,"On all Tesla cars",40))))
        }
    }
}