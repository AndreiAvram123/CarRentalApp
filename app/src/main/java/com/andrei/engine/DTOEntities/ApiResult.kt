package com.andrei.engine.DTOEntities

import com.google.gson.annotations.SerializedName

data class ApiResult<out T>(
        @SerializedName("isSuccessful")
        val isSuccessful : Boolean,
        @SerializedName("data")
        val data : T? ,
        @SerializedName("errors")
         val errors : List<String>?
         )

