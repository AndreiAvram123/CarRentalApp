package com.andrei.UI.adapters.bookings

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.andrei.carrental.entities.Booking
import com.andrei.carrental.entities.BookingDate

abstract class ViewHolderBooking(layout: View):RecyclerView.ViewHolder(layout){
    abstract fun bind(booking:Booking)
}
