package com.andrei.engine.responseModels

import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("token")
    val token:String
)
