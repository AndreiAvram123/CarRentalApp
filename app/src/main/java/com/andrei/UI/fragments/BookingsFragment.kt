package com.andrei.UI.fragments

import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.andrei.UI.adapters.CustomDivider
import com.andrei.UI.adapters.bookings.BookingsAdapter
import com.andrei.carrental.R
import com.andrei.carrental.databinding.FragmentBookingsLayoutBinding
import com.andrei.carrental.entities.Booking
import com.andrei.carrental.viewmodels.ViewModelBookings
import com.andrei.engine.State
import com.andrei.utils.hide
import com.andrei.utils.reObserve
import com.andrei.utils.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookingsFragment(
        private val bookingType:BookingType) :BaseFragment(R.layout.fragment_bookings_layout){

    enum class BookingType{
        PREVIOUS,
        CURRENT,
        UPCOMING
    }

     val binding: FragmentBookingsLayoutBinding by viewBinding()

    private val observerBookings = Observer<State<List<Booking>>>{
        when(it){
            is State.Success -> {
                updateRecyclerView(it.data)
                binding.pbBookings.hide()
            }
            is State.Loading -> {
                binding.pbBookings.show()
            }
            is State.Error ->{
                binding.pbBookings.hide()
            }
        }
    }



    private val viewModelBookings:ViewModelBookings by viewModels()


     private val bookingsAdapter:BookingsAdapter by lazy {
         BookingsAdapter(bookingType)
     }


    override fun initializeUI() {
       initializeRecyclerView()
        when(bookingType){
            BookingType.PREVIOUS->viewModelBookings.previousBookings.reObserve(viewLifecycleOwner,observerBookings)
            BookingType.CURRENT->viewModelBookings.currentBookings.reObserve(viewLifecycleOwner,observerBookings)
            BookingType.UPCOMING->viewModelBookings.upcomingBookings.reObserve(viewLifecycleOwner,observerBookings)
        }

    }

    private fun updateRecyclerView(bookings:List<Booking>?){
        if(bookings.isNullOrEmpty()){

        }else{
            bookingsAdapter.setNewBookings(bookings)
        }
    }

    private fun initializeRecyclerView() {
        binding.rvBookings.apply {
            addItemDecoration(CustomDivider(10))
            layoutManager = LinearLayoutManager(context)
            adapter = bookingsAdapter}
    }
}