package com.andrei.engine.DTOEntities

import com.google.gson.annotations.SerializedName

data class RentInformation(
        @SerializedName("bool")
        val bookingDTO: BookingDTO,
        @SerializedName("carToRentID")
        val carToRentID:Long

)