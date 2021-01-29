package com.andrei.engine.repositoryInterfaces

import com.andrei.engine.DTOEntities.LoginResponse
import com.andrei.engine.configuration.CallWrapper
import com.andrei.engine.requestModels.LoginRequest
import retrofit2.http.Body
import retrofit2.http.POST


interface AuthRepoInterface {
    @POST("/login")
    fun attemptLogin(@Body loginRequest: LoginRequest): CallWrapper<LoginResponse>
}