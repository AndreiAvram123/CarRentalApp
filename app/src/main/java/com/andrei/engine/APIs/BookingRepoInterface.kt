package com.andrei.engine.repositoryInterfaces

import com.andrei.engine.DTOEntities.BookingDTO
import com.andrei.engine.configuration.APIResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface BookingRepoInterface {

@GET("/users/{userID}/bookings")
suspend fun getBookings(@Path("userID") userID:Long): Response<APIResponse<List<BookingDTO>>>
}