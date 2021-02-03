package com.andrei.UI.adapters.bookings

import android.content.Context
import android.view.View
import com.andrei.carrental.R
import com.andrei.carrental.databinding.PreviousBookingLayoutBinding
import com.andrei.carrental.entities.Booking
import com.andrei.utils.formatWithPattern

class ViewHolderPreviousBooking(
        private val binding: PreviousBookingLayoutBinding) : BaseViewHolderBooking(binding.root) {

    override fun bind(booking: Booking) {
      binding.booking = booking
    }
}