package com.andrei.engine.repositoryInterfaces

import com.andrei.engine.configuration.ApiResult
import com.andrei.engine.responseModels.TokenResponse
import com.andrei.engine.configuration.CallWrapper
import com.andrei.engine.requestModels.NewBookingRequestModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PaymentRepoInterface {
  @GET("/sandboxToken")
  fun fetchClientToken(): Response<ApiResult<TokenResponse>>

  @POST("/checkout")
  fun checkout(@Body checkoutRequest: NewBookingRequestModel):Response<ApiResult<Nothing>>
}