package com.andrei.engine.repositoryInterfaces

import com.andrei.engine.DTOEntities.ApiResult
import com.andrei.engine.DTOEntities.CheckoutRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PaymentRepoInterface {
  @GET("/sandboxToken")
  fun fetchClientToken(): Call<ApiResult<TokenResponse>>

  @POST("/checkout")
  fun checkout(@Body checkoutRequest: CheckoutRequest):Call<ApiResult<Any>>
}