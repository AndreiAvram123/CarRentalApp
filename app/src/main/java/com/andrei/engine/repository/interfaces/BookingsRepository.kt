package com.andrei.engine.repository.interfaces

import androidx.lifecycle.LiveData
import com.andrei.carrental.entities.Booking
import com.andrei.engine.DTOEntities.BookingDTO
import com.andrei.engine.State
import kotlinx.coroutines.flow.Flow

interface BookingsRepository {

     fun fetchBookings(): Flow<State<List<BookingDTO>>>
}