package com.andrei.UI.adapters.bookings

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.andrei.carrental.entities.Booking
import com.andrei.carrental.entities.Chat

abstract class ViewHolderChat(layout:View) : RecyclerView.ViewHolder(layout) {
    abstract fun bind(chat:Chat)
}