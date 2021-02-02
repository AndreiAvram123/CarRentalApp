package com.andrei.engine.repositoryInterfaces

import com.andrei.engine.DTOEntities.ApiResult
import com.andrei.engine.DTOEntities.TokenResponse
import com.andrei.engine.configuration.CallWrapper
import com.andrei.engine.requestModels.NewBookingRequestModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PaymentRepoInterface {
  @GET("/sandboxToken")
  fun fetchClientToken(): CallWrapper<TokenResponse>

  @POST("/checkout")
  fun checkout(@Body checkoutRequest: NewBookingRequestModel):CallWrapper<Any>
}