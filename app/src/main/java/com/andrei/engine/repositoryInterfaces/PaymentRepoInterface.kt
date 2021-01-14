package com.andrei.engine.repositoryInterfaces

import com.andrei.engine.DTOEntities.ApiResult
import com.andrei.engine.DTOEntities.CheckoutRequest
import com.andrei.engine.DTOEntities.TokenResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PaymentRepoInterface {
  @GET("/payment/sandboxToken")
  fun fetchClientToken(): Call<TokenResponse>

  @POST("/payment/checkout")
  fun checkout(@Body checkoutRequest: CheckoutRequest):Call<ApiResult<Any>>
}