package com.andrei.engine.responseModels

import com.google.gson.annotations.SerializedName

data class RegistrationFieldValidResponse (
        @SerializedName("valid")
        val valid:Boolean,
        @SerializedName("reason")
        val reason:String?
)