package com.andrei.carrental.entities

import com.andrei.engine.DTOEntities.GeoPoint
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
        @SerializedName("location")
        val location:GeoPoint,
        @SerializedName("car")
       val carModel : CarModel
)
