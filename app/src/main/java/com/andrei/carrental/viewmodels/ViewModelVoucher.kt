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

    private val _availableVouchers: MutableStateFlow<State<MutableList<Voucher>>> =
        MutableStateFlow(State.Loading)

    val availableVouchers: StateFlow<State<List<Voucher>>>
        get() = _availableVouchers.asStateFlow()

    private val _allVouchersDismissed: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val noVouchersAvailable: StateFlow<Boolean>
        get() = _allVouchersDismissed.asStateFlow()


    fun getVouchers(userID: Long) {
        viewModelScope.launch {

        }
    }

    fun redeemLastVoucher() {
        val currentVouchersState = _availableVouchers.value
        if (currentVouchersState is State.Success) {
            removeLastVoucher(currentVouchersState.data)
        }
    }

    private fun removeLastVoucher(data: MutableList<Voucher>) {
        data.removeAt(0)
        if (data.isEmpty()) {
            viewModelScope.launch {
                _allVouchersDismissed.emit(true)
            }
        }

    }
}