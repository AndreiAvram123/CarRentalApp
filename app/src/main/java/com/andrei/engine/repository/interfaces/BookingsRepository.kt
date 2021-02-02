package com.andrei.engine.repository.interfaces

import androidx.lifecycle.LiveData
import com.andrei.engine.DTOEntities.BookingDTO
import com.andrei.engine.State

interface BookingsRepository {
     val bookings: LiveData<State<List<BookingDTO>>>
}