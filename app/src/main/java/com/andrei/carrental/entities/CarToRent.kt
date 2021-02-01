package com.andrei.carrental.entities

import com.google.gson.annotations.SerializedName

data class CarToRent(
        @SerializedName("id")
        val id:Long,
        @SerializedName("images")
       val images:List<Image>,
        @SerializedName("pricePerDay")
        val pricePerDay:Double,
        @SerializedName("basicUser")
        val basicUser: BasicUser,
        @SerializedName("latitude")
       val latitude:Double,
        @SerializedName("longitude")
       val longitude:Double,
        @SerializedName("car")
       val carModel : CarModel
)
