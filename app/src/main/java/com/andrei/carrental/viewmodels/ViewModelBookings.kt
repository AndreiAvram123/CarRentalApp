package com.andrei.carrental.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.andrei.carrental.entities.Booking
import com.andrei.engine.DTOEntities.toBooking
import com.andrei.engine.State
import com.andrei.engine.repository.interfaces.BookingsRepository
import com.andrei.utils.toDate
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ViewModelBookings @Inject constructor(
        private val bookingsRepository: BookingsRepository
) : ViewModel() {

    private val currentDate  = Date()

    val previousBookings:LiveData<State<List<Booking>>> = Transformations.map(bookingsRepository.bookings){state->
        when(state){
            is State.Error -> state
            is State.Success -> {
               val newData = state.data?.map { it.toBooking()}?.filter { it.isPreviousBooking() }
                return@map  State.Success(newData)
            }
            is State.Loading -> state
        }
    }
    val currentBookings :LiveData<State<List<Booking>>> = Transformations.map(bookingsRepository.bookings){
        state ->
        when(state){
             is State.Error -> state
            is State.Loading -> state
            is State.Success -> State.Success(state.data?.map { it.toBooking()}?.filter { it.isCurrentBooking() })
        }
    }

    val upcomingBookings :LiveData<State<List<Booking>>> = Transformations.map(bookingsRepository.bookings){
        state ->
        when(state){
            is State.Error -> state
            is State.Loading -> state
            is State.Success -> State.Success(state.data?.map { it.toBooking()}?.filter { it.isUpcomingBooking() })
        }
    }



    private fun Booking.isCurrentBooking():Boolean{
        return date.startDate.toDate().before(currentDate) && date.endDate.toDate().after(currentDate)
    }
    private fun Booking.isPreviousBooking():Boolean{
        return date.endDate.toDate().before(currentDate)
    }
    private fun Booking.isUpcomingBooking():Boolean{
       return date.startDate.toDate().after(currentDate)
    }

}