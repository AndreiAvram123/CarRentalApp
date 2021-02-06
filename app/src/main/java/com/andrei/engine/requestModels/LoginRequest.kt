package com.andrei.engine.requestModels

import com.google.gson.annotations.SerializedName

data class LoginRequest(
        @SerializedName("username")
        val email:String,
        @SerializedName("password")
        val password :String
)