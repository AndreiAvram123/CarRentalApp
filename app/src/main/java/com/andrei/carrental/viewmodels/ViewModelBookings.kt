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

    private val _bookings: MutableStateFlow<State<List<Booking>>> = MutableStateFlow(State.Loading)
    val bookings: StateFlow<State<List<Booking>>>
        get() = _bookings


    init {
        viewModelScope.launch {
            try {
                bookingsRepository.fetchBookings().collect { state ->
                    _bookings.emit(
                        when (state) {
                            is State.Error -> state
                            is State.Loading -> state
                            is State.Success -> State.Success(state.data?.map { it.toBooking() })
                        }
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }
}