package com.andrei.engine.responseModels

import com.andrei.engine.DTOEntities.BasicUserLoginData
import com.google.gson.annotations.SerializedName

data class LoginResponse(
        @SerializedName("basicUserLoginData")
        val basicUserLoginData: BasicUserLoginData,
        @SerializedName("token")
        val token :String
)
