package com.andrei.engine.DTOEntities

import com.google.gson.annotations.SerializedName

data class RentInformation(
        @SerializedName("bool")
        val bookingDateDTO: BookingDateDTO,
        @SerializedName("carToRentID")
        val carToRentID:Long

)