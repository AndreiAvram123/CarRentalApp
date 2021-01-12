package com.andrei.engine.repositoryInterfaces

import com.andrei.carrental.entities.CarSearchEntity
import com.andrei.engine.CarToRent
import com.andrei.engine.repository.CarRepositoryImpl
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CarRepoInterface {
    @GET("/cars/nearby")
    fun getNearbyCars(@Query("latitude") latitude:Double,
                      @Query("longitude") longitude:Double): Call<List<CarToRent>>

    @GET("/cars/search")
    fun search(@Query("latitude") latitude:Double,
                      @Query("longitude") longitude:Double,
                      @Query("query") query: String): Call<List<CarSearchEntity>>
}