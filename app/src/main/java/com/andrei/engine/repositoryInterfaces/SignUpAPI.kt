package com.andrei.engine.repositoryInterfaces

import com.andrei.engine.responseModels.UsernameValidResponse
import com.andrei.engine.configuration.CallWrapper
import com.andrei.engine.requestModels.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SignUpAPI {

    @GET("/register/usernameAvailable")
    fun checkIfUsernameIsAvailable(@Query("username") username:String): CallWrapper<UsernameValidResponse>

    @POST("/register")
    fun register(@Body registerRequest: RegisterRequest):CallWrapper<Nothing>
}