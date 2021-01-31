package com.andrei.engine.repositoryInterfaces

import com.andrei.engine.DTOEntities.UsernameValidResponse
import com.andrei.engine.configuration.CallWrapper
import retrofit2.http.GET
import retrofit2.http.Query

interface SignUpRepositoryInterface {

    @GET("/register/usernameAvailable")
    fun checkIfUsernameIsAvailable(@Query("username") username:String): CallWrapper<UsernameValidResponse>
}