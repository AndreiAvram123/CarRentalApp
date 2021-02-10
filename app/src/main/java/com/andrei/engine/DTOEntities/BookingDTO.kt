package com.andrei.engine.DTOEntities

import com.andrei.carrental.entities.Booking
import com.andrei.carrental.entities.Car
import com.google.gson.annotations.SerializedName

data class BookingDTO(
        @SerializedName("date")
        val date: BookingDateDTO,
        @SerializedName("carRented")
        val carRented: Car
)
fun BookingDTO.toBooking(): Booking = Booking(date = this.date.toBookingDate(),car = this.carRented )
