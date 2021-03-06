package com.andrei.carrental.entities

import com.andrei.engine.DTOEntities.GeoPoint
import com.andrei.engine.DTOEntities.UserDTO
import com.google.gson.annotations.SerializedName

data class Car(
        @SerializedName("id")
        val id:Long,
        @SerializedName("mediaFiles")
        val mediaFiles:List<MediaFile>,
        @SerializedName("pricePerDay")
        val pricePerDay:Double,
        @SerializedName("user")
        val basicUser: UserDTO,
        @SerializedName("location")
        val location:GeoPoint,
        @SerializedName("distanceFromLocation")
        val distanceFromLocation:Double,
        @SerializedName("carModel")
       val carModel : CarModel
)
