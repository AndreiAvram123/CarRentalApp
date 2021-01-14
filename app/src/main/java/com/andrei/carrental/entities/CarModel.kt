package com.andrei.carrental.entities

import com.google.gson.annotations.SerializedName

data class CarModel(
        @SerializedName("name")
        val name:String
)
