package com.andrei.engine.repositoryInterfaces

import com.andrei.engine.DTOEntities.TokenResponse
import com.andrei.engine.configuration.CallWrapper
import retrofit2.Call
import retrofit2.http.GET


interface AuthRepoInterface {
   @GET("/token")
   fun getToken(): CallWrapper<TokenResponse>
}