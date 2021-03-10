package com.andrei.engine.repositoryInterfaces

import com.andrei.engine.configuration.APIResponse
import com.andrei.engine.responseModels.LoginResponse
import com.andrei.engine.configuration.CallWrapper
import com.andrei.engine.requestModels.LoginRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface LoginAPI {
    @POST("/login")
    suspend fun attemptLogin(@Body loginRequest: LoginRequest): Response<APIResponse<LoginResponse>>
}