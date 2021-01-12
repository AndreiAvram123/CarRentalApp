package com.andrei.engine.repositoryInterfaces

import com.andrei.engine.CarToRent
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CarRepoInterface {
    @GET("/cars/nearby")
    fun getNearbyCars(@Query("latitude") latitude:Double,
                      @Query("longitude") longitude:Double): Call<List<CarToRent>>
}