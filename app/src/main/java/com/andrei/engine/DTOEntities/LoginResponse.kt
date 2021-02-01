package com.andrei.engine.DTOEntities

import com.google.gson.annotations.SerializedName


data class LoginResponse(
        @SerializedName("basicUserLoginData")
        val basicUserLoginData: BasicUserLoginData,
        @SerializedName("token")
        val token :String
)
