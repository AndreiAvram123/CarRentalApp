package com.andrei.engine.DTOEntities

import com.andrei.carrental.entities.CarToRent
import com.google.gson.annotations.SerializedName

data class RentInformation(
        @SerializedName("rentPeriod")
        val rentalPeriodDTO: RentalPeriodDTO,
        @SerializedName("carToRentID")
        val carToRentID:Long

)