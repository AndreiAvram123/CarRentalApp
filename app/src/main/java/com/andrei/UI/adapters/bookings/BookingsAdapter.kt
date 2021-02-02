package com.andrei.UI.adapters.bookings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andrei.carrental.databinding.PreviousBookingLayoutBinding
import com.andrei.carrental.entities.Booking

class BookingsAdapter(private val bookingType:BookingType ): RecyclerView.Adapter<BaseViewHolderBooking>() {

    private val bookings:MutableList<Booking> = mutableListOf()

    enum class BookingType{
        PREVIOUS,
        CURRENT,
        UPCOMING
    }

    fun setNewBookings(newBookings:List<Booking>){
        bookings.clear()
        bookings.addAll(newBookings)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = bookings.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolderBooking {
        when (bookingType) {
            BookingType.PREVIOUS -> {
                val binding = PreviousBookingLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                return ViewHolderPreviousBooking(binding)
            }
            else -> {
                val binding = PreviousBookingLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ViewHolderPreviousBooking(binding)
            }
        }
     }

    override fun onBindViewHolder(holder: BaseViewHolderBooking, position: Int) {
        holder.bind(bookings[position])
    }

}