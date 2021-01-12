package com.andrei.engine

import com.andrei.carrental.entities.Car
import com.google.gson.annotations.SerializedName

data class CarToRent(
        @SerializedName("id")
        val id:Long,
        @SerializedName("latitude")
        val latitude : Double,
        @SerializedName("longitude")
        val longitude:Double,
        @SerializedName("car")
        val car:Car,
        @SerializedName("imagePath")
        val imagePath:String
)
