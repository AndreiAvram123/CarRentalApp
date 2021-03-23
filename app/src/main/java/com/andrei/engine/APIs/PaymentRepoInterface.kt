package com.andrei.engine.APIs

import com.andrei.engine.configuration.APIResponse
import com.andrei.engine.responseModels.TokenResponse
import com.andrei.engine.requestModels.NewBookingRequestModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PaymentRepoInterface {
  @GET("/sandboxToken")
  suspend fun fetchClientToken(): Response<APIResponse<TokenResponse>>

  @POST("/checkout")
  suspend fun checkout(@Body checkoutRequest: NewBookingRequestModel):Response<APIResponse<Nothing>>
}