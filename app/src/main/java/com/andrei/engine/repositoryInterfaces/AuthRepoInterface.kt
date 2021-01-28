package com.andrei.engine.repositoryInterfaces

import com.andrei.engine.DTOEntities.LoginResponse
import com.andrei.engine.configuration.CallWrapper
import retrofit2.http.POST


interface AuthRepoInterface {
    @POST
    fun attemptLogin(): CallWrapper<LoginResponse>
}