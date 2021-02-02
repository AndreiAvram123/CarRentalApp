package com.andrei.engine.repository.implementation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import com.andrei.carrental.entities.Booking
import com.andrei.engine.CallRunner
import com.andrei.engine.DTOEntities.BookingDTO
import com.andrei.engine.State
import com.andrei.engine.helpers.UserManager
import com.andrei.engine.repository.interfaces.BookingsRepository
import com.andrei.engine.repositoryInterfaces.BookingRepoInterface
import javax.inject.Inject

class BookingsRepositoryImpl @Inject constructor(
        userManager: UserManager,
        private val callRunner: CallRunner,
        private val bookingRepo:BookingRepoInterface
) : BookingsRepository {

    override val bookings:LiveData<State<List<BookingDTO>>> = Transformations.switchMap(userManager.userLoginData){
        fetchBookings(it.id)
    }

    private fun fetchBookings(userID:Int?) : LiveData<State<List<BookingDTO>>> = liveData<State<List<BookingDTO>>>{
          if(userID == null){
              emit(State.Success(emptyList()))
          }else {
              callRunner.makeApiCall(bookingRepo.getBookings(userID)) {
                  emit(it)
              }
          }

    }

}