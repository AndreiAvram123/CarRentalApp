package com.andrei.carrental.entities

data class CheckoutCarData (
    val carID:Long,
    val bookingDate: BookingDate,
   val amount:Double)