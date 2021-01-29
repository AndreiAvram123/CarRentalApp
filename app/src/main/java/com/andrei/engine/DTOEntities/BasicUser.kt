package com.andrei.engine.DTOEntities

import com.google.gson.annotations.SerializedName

data class BasicUser(
        @SerializedName("email")
        val email:String,
        @SerializedName("id")
        val id :Int
)
