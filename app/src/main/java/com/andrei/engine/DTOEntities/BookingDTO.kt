package com.andrei.engine.DTOEntities

import com.andrei.carrental.entities.Booking
import com.andrei.carrental.entities.CarToRent
import com.google.gson.annotations.SerializedName

data class BookingDTO(
        @SerializedName("date")
        val bookingDate: BookingDateDTO,
        @SerializedName("rentedCar")
        val car: CarToRent
)
fun BookingDTO.toBooking(): Booking = Booking(bookingDate = this.bookingDate.toBookingDate(),car = this.car )
