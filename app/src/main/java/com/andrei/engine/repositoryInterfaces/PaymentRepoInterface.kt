package com.andrei.engine.repositoryInterfaces

import com.andrei.engine.DTOEntities.TokenResponse
import retrofit2.Call
import retrofit2.http.GET

interface PaymentRepoInterface {
  @GET("/payment/sandboxToken")
  fun fetchClientToken(): Call<TokenResponse>
}