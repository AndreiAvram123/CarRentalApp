package com.andrei.engine.DTOEntities

import com.andrei.carrental.entities.BookingDate
import com.andrei.utils.fromUnixToLocalDate
import com.andrei.utils.toUnix
import com.google.gson.annotations.SerializedName

data class BookingDTO(
        @SerializedName("startDate")
        val startDate:Long,
        @SerializedName("endDate")
        val endDate:Long
)


fun BookingDTO.toBooking():BookingDate{
    return BookingDate(startDate = this.startDate.fromUnixToLocalDate(),endDate = this.endDate.fromUnixToLocalDate())
}
fun BookingDate.toBookingDTO():BookingDTO{
    return BookingDTO(startDate = this.startDate.toUnix(),endDate = this.endDate.toUnix())
}
