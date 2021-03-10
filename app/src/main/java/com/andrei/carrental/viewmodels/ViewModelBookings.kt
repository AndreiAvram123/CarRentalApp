package com.andrei.carrental.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andrei.carrental.entities.Booking
import com.andrei.engine.DTOEntities.toBooking
import com.andrei.engine.State
import com.andrei.engine.repository.interfaces.BookingsRepository
import com.andrei.utils.toDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ViewModelBookings @Inject constructor(
        private val bookingsRepository: BookingsRepository
) : ViewModel() {

    private val _bookings: MutableStateFlow<State<List<Booking>>> by lazy {
        MutableStateFlow<State<List<Booking>>>(State.Loading).also {
            getBookings()
        }
    }

    val bookings: StateFlow<State<List<Booking>>>
        get() = _bookings


    private fun getBookings(){
        viewModelScope.launch {
            bookingsRepository.fetchBookings().collect { state ->
                when (state) {
                    is State.Error -> _bookings.emit(state)
                    is State.Loading -> _bookings.emit(state)
                    is State.Success -> _bookings.emit(State.Success(state.data.map { it.toBooking() }))
                }
            }
        }

    }



}