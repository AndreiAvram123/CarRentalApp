package com.andrei.engine.configuration

import com.google.gson.annotations.SerializedName

data class APIResponse<out T>(
        @SerializedName("isSuccessful")
        val isSuccessful : Boolean,
        @SerializedName("data")
        val data : T)



data class ApiError(
        @SerializedName("error")
        val message:String
)