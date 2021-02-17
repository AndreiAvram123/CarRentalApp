package com.andrei.engine.repositoryInterfaces

import com.andrei.engine.DTOEntities.BookingDTO
import com.andrei.engine.DTOEntities.BookingDateDTO
import com.andrei.engine.configuration.CallWrapper
import retrofit2.http.GET
import retrofit2.http.Path

interface BookingRepoInterface {
@GET("/users/{userID}/bookings")
fun getBookings(@Path("userID") userID:Long): CallWrapper<List<BookingDTO>>
}