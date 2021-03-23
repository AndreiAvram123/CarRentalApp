package com.andrei.engine.APIs

import com.andrei.engine.DTOEntities.ChatDTO
import com.andrei.engine.DTOEntities.UserDTO
import com.andrei.engine.configuration.APIResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface UserAPI {
    @GET("/users/{userID}")
    fun getUser(@Path("userID") userID:Long): Response<APIResponse<List<UserDTO>>>
}