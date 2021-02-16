package com.andrei.UI.adapters.bookings

import com.andrei.carrental.databinding.PreviousBookingLayoutBinding
import com.andrei.carrental.entities.Booking

class ViewHolderPreviousBooking(
        private val binding: PreviousBookingLayoutBinding) : BaseViewHolderBooking(binding.root) {

    override fun bind(booking: Booking) {
      binding.booking = booking
    }
}