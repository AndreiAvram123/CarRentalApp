package com.andrei.carrental.entities

import com.andrei.UI.fragments.BookingsFragment
import com.andrei.utils.toDate
import java.util.*

data class Booking(
        val date: BookingDate,
        val car: Car
)

enum class BookingType{
        PREVIOUS,
        CURRENT,
        UPCOMING
}


 fun Booking.isCurrentBooking():Boolean{
        val currentDate  = Date()
        return date.startDate.toDate().before(currentDate) && date.endDate.toDate().after(currentDate)
}
 fun Booking.isPreviousBooking():Boolean{
        val currentDate  = Date()
        return date.endDate.toDate().before(currentDate)
}
 fun Booking.isUpcomingBooking():Boolean{
        val currentDate  = Date()
        return date.startDate.toDate().after(currentDate)
}
