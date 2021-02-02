package com.andrei.carrental.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.andrei.UI.fragments.BaseFragment
import com.andrei.carrental.entities.Booking
import com.andrei.engine.DTOEntities.toBooking
import com.andrei.engine.State
import com.andrei.engine.repository.interfaces.BookingsRepository
import java.util.*

class ViewModelBookings @ViewModelInject constructor(
        private val bookingsRepository: BookingsRepository
) : ViewModel() {

    val previousBookings:LiveData<State<List<Booking>>> = Transformations.map(bookingsRepository.bookings){state->
        when(state){
            is State.Error -> state
            is State.Success -> {
               val newData = state.data?.filter { it.bookingDate.endDate < System.currentTimeMillis() }?.map { it.toBooking() }
                return@map  State.Success(newData)
            }
            is State.Loading -> state
        }
    }

}