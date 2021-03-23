package com.andrei.UI.fragments

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.andrei.UI.adapters.CustomDivider
import com.andrei.UI.adapters.bookings.BookingsAdapter
import com.andrei.carrental.R
import com.andrei.carrental.databinding.FragmentBookingsLayoutBinding
import com.andrei.carrental.entities.*
import com.andrei.carrental.viewmodels.ViewModelBookings
import com.andrei.engine.State
import com.andrei.utils.hide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BookingsFragment(
        private val bookingType:BookingType) :BaseFragment(R.layout.fragment_bookings_layout){


     val binding: FragmentBookingsLayoutBinding by viewBinding()

    private val viewModelBookings:ViewModelBookings by viewModels()


     private val bookingsAdapter:BookingsAdapter by lazy {
         BookingsAdapter(bookingType)
     }


    override fun initializeUI() {
        initializeRecyclerView()
        lifecycleScope.launch {
            viewModelBookings.bookings.collect { state ->
                when (state) {
                    is State.Success -> {
                            setAdapterData(state.data)
                    }
                }

            }

        }
    }
    private fun setAdapterData(data:List<Booking>){
        val filteredData = when (bookingType) {
            BookingType.PREVIOUS -> data.filter { it.isPreviousBooking() }
            BookingType.UPCOMING -> data.filter { it.isUpcomingBooking() }
            else -> data.filter { it.isCurrentBooking() }
        }
        bookingsAdapter.setNewBookings(filteredData)
        binding.pbBookings.hide()
    }


    private fun initializeRecyclerView() {
        binding.rvBookings.apply {
            addItemDecoration(CustomDivider(10))
            layoutManager = LinearLayoutManager(context)
            adapter = bookingsAdapter}
    }
}