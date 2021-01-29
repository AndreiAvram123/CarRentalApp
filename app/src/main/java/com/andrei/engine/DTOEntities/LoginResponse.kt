package com.andrei.engine.DTOEntities

import com.andrei.carrental.entities.User
import com.google.gson.annotations.SerializedName


data class LoginResponse(
        @SerializedName("basicUser")
        val user:BasicUser,
        @SerializedName("token")
        val token :String
)
