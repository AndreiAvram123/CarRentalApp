package com.andrei.engine.DTOEntities

import com.andrei.carrental.entities.Booking
import com.andrei.carrental.entities.CarToRent
import com.google.gson.annotations.SerializedName

data class BookingDTO(
        @SerializedName("date")
        val date: BookingDateDTO,
        @SerializedName("carRented")
        val carRented: CarToRent
)
fun BookingDTO.toBooking(): Booking = Booking(date = this.date.toBookingDate(),car = this.carRented )
