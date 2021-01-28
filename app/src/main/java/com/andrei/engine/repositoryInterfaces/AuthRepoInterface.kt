package com.andrei.engine.repositoryInterfaces

import com.andrei.engine.configuration.CallWrapper
import retrofit2.http.GET


interface AuthRepoInterface {
   @GET("/token")
   fun getToken(): CallWrapper<TokenResponse>
}