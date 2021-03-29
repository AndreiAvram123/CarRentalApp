package com.andrei.engine.requestModels

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
        @SerializedName("username")
        val username:String,
        @SerializedName("email")
        val email:String,
        @SerializedName("password")
        val password:String,
        @SerializedName("base64ProfilePicture")
        val base64ProfilePicture:String
)
