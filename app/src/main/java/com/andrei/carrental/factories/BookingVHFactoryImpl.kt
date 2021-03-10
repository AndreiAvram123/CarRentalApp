package com.andrei.carrental.factories

import android.view.LayoutInflater
import android.view.ViewGroup
import com.andrei.UI.adapters.bookings.ViewHolderPreviousBooking

import com.andrei.carrental.databinding.PreviousBookingLayoutBinding
import com.andrei.carrental.entities.BookingType


interface BookingsVHFactory {
    fun create(parent: ViewGroup,bookingType: BookingType):ViewHolderPreviousBooking
}


class BookingsVHFactoryImpl :BookingsVHFactory {
    override fun create(parent: ViewGroup, bookingType: BookingType): ViewHolderPreviousBooking {
        return when (bookingType) {
            BookingType.PREVIOUS -> {
                val binding = PreviousBookingLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                ViewHolderPreviousBooking(binding)
            }
            else -> {
                val binding = PreviousBookingLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ViewHolderPreviousBooking(binding)
            }
        }
    }

}