package com.andrei.engine.repositoryInterfaces

import com.andrei.carrental.entities.CarSearchEntity
import com.andrei.carrental.entities.CarToRent
import com.andrei.carrental.entities.RentalPeriod
import com.andrei.engine.DTOEntities.ApiResult
import com.andrei.engine.DTOEntities.RentalPeriodDTO
import retrofit2.Call
import retrofit2.http.*

interface CarRepoInterface {
    @GET("/cars/nearby")
    fun getNearbyCars(@Query("latitude") latitude:Double,
                      @Query("longitude") longitude:Double): Call<ApiResult<List<CarToRent>>>

    @GET("/cars/search")
    fun search(@Query("latitude") latitude:Double,
                      @Query("longitude") longitude:Double,
                      @Query("query") query: String): Call<ApiResult<List<CarSearchEntity>>>

    @GET("/cars/{carID}")
    fun getCarByID(@Path("carID") carID:Long):Call<ApiResult<CarToRent>>

    @GET("/cars/{carID}/unavailableDates")
    fun getUnavailableDates(@Path("carID") carID:Long):Call<ApiResult<List<RentalPeriodDTO>>>

}