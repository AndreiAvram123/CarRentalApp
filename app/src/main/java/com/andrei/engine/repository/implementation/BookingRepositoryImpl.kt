package com.andrei.engine.repository.implementation

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import com.andrei.carrental.entities.Booking
import com.andrei.engine.CallRunner
import com.andrei.engine.DTOEntities.BookingDTO
import com.andrei.engine.State
import com.andrei.engine.helpers.UserManager
import com.andrei.engine.repository.interfaces.BookingRepository
import com.andrei.engine.repositoryInterfaces.BookingRepoInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BookingRepositoryImpl @Inject constructor(
        private val userManager: UserManager,
        private val callRunner: CallRunner,
        private val bookingRepo:BookingRepoInterface
) : BookingRepository {

    val bookings:LiveData<State<List<BookingDTO>>> = Transformations.switchMap(userManager.userLoginData){
        fetchBookings(it.id)
    }

    private fun fetchBookings(userID:Int?) : LiveData<State<List<BookingDTO>>> = liveData {
          if(userID == null){
              emit(State.Success(emptyList()))
              return@liveData
          }
          callRunner.makeApiCall(bookingRepo.getBookings(userID)){
              emit(it)
          }

    }

}