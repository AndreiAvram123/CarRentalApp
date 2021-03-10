package com.andrei.engine.repository.implementation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import com.andrei.carrental.UserDataManager
import com.andrei.carrental.entities.Booking
import com.andrei.engine.CallRunner
import com.andrei.engine.DTOEntities.BookingDTO
import com.andrei.engine.State
import com.andrei.engine.helpers.UserManager
import com.andrei.engine.repository.interfaces.BookingsRepository
import com.andrei.engine.repositoryInterfaces.BookingRepoInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BookingsRepositoryImpl @Inject constructor(
        private val callRunner: CallRunner,
        private val bookingRepo:BookingRepoInterface,
        private val userDataManager: UserDataManager
) : BookingsRepository {


    override fun fetchBookings() : Flow<State<List<BookingDTO>>> =
              callRunner.makeApiCall{
                  bookingRepo.getBookings(userDataManager.getUserID())
              }

}