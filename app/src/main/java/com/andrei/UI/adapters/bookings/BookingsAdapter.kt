package com.andrei.UI.adapters.bookings

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andrei.carrental.entities.Booking
import com.andrei.carrental.entities.BookingType
import com.andrei.carrental.factories.BookingsVHFactory
import com.andrei.carrental.factories.BookingsVHFactoryImpl

class BookingsAdapter(private val bookingType: BookingType): RecyclerView.Adapter<ViewHolderBooking>() {

    private val bookings:MutableList<Booking> = mutableListOf()
    private val factory: BookingsVHFactory = BookingsVHFactoryImpl()

    fun setNewBookings(newBookings:List<Booking>){
        bookings.clear()
        bookings.addAll(newBookings)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = bookings.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderBooking = factory.create(parent,bookingType)

    override fun onBindViewHolder(holderBooking: ViewHolderBooking, position: Int) {
        holderBooking.bind(bookings[position])
    }

}