package com.andrei.engine.DTOEntities

import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("token")
    val token:String
)
