package com.andrei.engine.repositoryInterfaces

import com.andrei.carrental.entities.Car
import com.andrei.engine.configuration.ApiResult
import com.andrei.engine.DTOEntities.BookingDateDTO
import com.andrei.engine.configuration.CallWrapper
import retrofit2.Call
import retrofit2.http.*

interface CarAPI {
    @GET("/cars/nearby")
    fun getNearbyCars(@Query("latitude") latitude:Double,
                      @Query("longitude") longitude:Double): Call<ApiResult<List<Car>>>

    @GET("/cars/search")
    fun search(@Query("latitude") latitude:Double,
                      @Query("longitude") longitude:Double,
                      @Query("query") query: String): Call<ApiResult<List<Car>>>

    @GET("/cars/{carID}")
    fun getCarByID(@Path("carID") carID:Long):Call<ApiResult<Car>>

    @GET("/cars/{carID}/unavailableDates")
    fun getUnavailableDates(@Path("carID") carID:Long):CallWrapper<List<BookingDateDTO>>

}