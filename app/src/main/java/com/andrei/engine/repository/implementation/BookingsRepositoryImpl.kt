package com.andrei.engine.repository.implementation

import com.andrei.carrental.UserDataManager
import com.andrei.carrental.entities.Booking
import com.andrei.engine.CallRunner
import com.andrei.engine.DTOEntities.BookingDTO
import com.andrei.engine.State
import com.andrei.engine.repository.interfaces.BookingsRepository
import com.andrei.engine.APIs.BookingRepoInterface
import com.andrei.engine.DTOEntities.toBooking
import com.andrei.engine.utils.mapState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

class BookingsRepositoryImpl @Inject constructor(
        private val callRunner: CallRunner,
        private val bookingRepo:BookingRepoInterface,
        private val userDataManager: UserDataManager
) : BookingsRepository {


    override fun fetchBookings() : Flow<State<List<Booking>>> = callRunner.makeApiCall{
        bookingRepo.getBookings(userDataManager.userID)
    }.transform {state->
        when (state) {
            is State.Error -> emit(state)
            is State.Loading -> emit(state)
            is State.Success -> {
                val transformedData = state.data.map { it.toBooking() }
                emit(State.Success(transformedData))
            }
        }
    }

}