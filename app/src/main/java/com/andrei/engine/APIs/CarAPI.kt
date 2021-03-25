package com.andrei.engine.APIs

import com.andrei.carrental.entities.Car
import com.andrei.engine.configuration.APIResponse
import com.andrei.engine.DTOEntities.BookingDateDTO
import retrofit2.Response
import retrofit2.http.*

interface CarAPI {

    @GET("/cars/search")
    suspend fun search(@Query("latitude") latitude:Double,
                      @Query("longitude") longitude:Double,
                      @Query("query") query: String?= null ): Response<APIResponse<List<Car>>>

    @GET("/cars/{carID}")
    suspend fun getCarByID(@Path("carID") carID:Long):Response<APIResponse<Car>>

    @GET("/cars/{carID}/unavailableDates")
    suspend fun getUnavailableDates(@Path("carID") carID:Long):Response<APIResponse<List<BookingDateDTO>>>

}