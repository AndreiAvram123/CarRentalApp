package com.andrei.engine.configuration

import com.google.gson.annotations.SerializedName

data class ApiResult<out T>(
        @SerializedName("isSuccessful")
        val isSuccessful : Boolean,
        @SerializedName("data")
        val data : T? ,
        @SerializedName("error")
         val error :String?)

