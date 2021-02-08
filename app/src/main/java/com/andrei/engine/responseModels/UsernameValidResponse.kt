package com.andrei.engine.responseModels

import com.google.gson.annotations.SerializedName

data class UsernameValidResponse (
        @SerializedName("usernameValid")
        val usernameValid:Boolean,
        @SerializedName("reason")
        val reason:String?
)