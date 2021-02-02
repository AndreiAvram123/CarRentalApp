package com.andrei.engine.repositoryInterfaces

import com.andrei.engine.DTOEntities.BookingDTO
import com.andrei.engine.DTOEntities.BookingDateDTO
import com.andrei.engine.configuration.CallWrapper
import retrofit2.http.GET

interface BookingRepoInterface {
@GET("")
fun getBookings(userID:Int): CallWrapper<List<BookingDTO>>
}