package com.andrei.engine.DTOEntities

import com.andrei.carrental.entities.BookingDate
import com.andrei.utils.fromUnixToLocalDate
import com.andrei.utils.toUnix
import com.google.gson.annotations.SerializedName

data class BookingDateDTO(
        @SerializedName("startDate")
        val startDate:Long,
        @SerializedName("endDate")
        val endDate:Long
)


fun BookingDateDTO.toBookingDate():BookingDate{
    return BookingDate(startDate =
    this.startDate.fromUnixToLocalDate(),
            endDate = this.endDate.fromUnixToLocalDate())
}
