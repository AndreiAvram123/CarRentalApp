package com.andrei.engine.repositoryInterfaces

import com.andrei.carrental.entities.Car
import com.andrei.carrental.entities.CarSearchEntity
import com.andrei.engine.CarToRent
import com.andrei.engine.repository.CarRepositoryImpl
import retrofit2.Call
import retrofit2.http.*

interface CarRepoInterface {
    @GET("/cars/nearby")
    fun getNearbyCars(@Query("latitude") latitude:Double,
                      @Query("longitude") longitude:Double): Call<List<CarToRent>>

    @GET("/cars/search")
    fun search(@Query("latitude") latitude:Double,
                      @Query("longitude") longitude:Double,
                      @Query("query") query: String): Call<List<CarSearchEntity>>

    @GET("/cars/{carID}")
    fun fetchCarByID(@Path("carID") carID:Long):Call<Car>

}