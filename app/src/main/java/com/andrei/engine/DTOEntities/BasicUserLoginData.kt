package com.andrei.engine.DTOEntities

import com.google.gson.annotations.SerializedName

data class BasicUserLoginData(
        @SerializedName("email")
        val email:String,
        @SerializedName("id")
        val id :Long
)
