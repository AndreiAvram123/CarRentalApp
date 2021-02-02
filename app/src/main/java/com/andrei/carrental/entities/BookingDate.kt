package com.andrei.carrental.entities

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class BookingDate(
        val startDate:LocalDate,
        val endDate:LocalDate
)


