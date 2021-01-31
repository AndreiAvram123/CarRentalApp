package com.andrei.engine.DTOEntities

import com.google.gson.annotations.SerializedName

data  class UsernameValidResponse (
        @SerializedName("usernameValid")
        val usernameValid:Boolean,
        @SerializedName("reason")
        val reason:String?
)