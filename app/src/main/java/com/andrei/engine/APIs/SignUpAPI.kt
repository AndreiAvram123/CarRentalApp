package com.andrei.engine.APIs

import com.andrei.engine.configuration.APIResponse
import com.andrei.engine.responseModels.RegistrationFieldValidResponse
import com.andrei.engine.requestModels.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SignUpAPI {

    @GET("/register/usernameAvailable")
    suspend fun checkIfUsernameIsAvailable(@Query("username") username:String): Response<APIResponse<RegistrationFieldValidResponse>>

    @GET("/register/emailAvailable")
   suspend fun checkIfEmailIsAvailable(@Query("email") email:String):Response<APIResponse<RegistrationFieldValidResponse>>

    @POST("/register")
  suspend  fun register(@Body registerRequest: RegisterRequest):Response<APIResponse<Nothing>>
}