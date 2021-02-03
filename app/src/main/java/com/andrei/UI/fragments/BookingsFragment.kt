package com.andrei.UI.fragments

import androidx.fragment.app.viewModels
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
class BookingsFragment :BaseFragment(R.layout.fragment_bookings_layout){

     val binding: FragmentBookingsLayoutBinding by viewBinding()

    private val viewModelBookings:ViewModelBookings by viewModels()
     private val bookingsAdapter:BookingsAdapter by lazy {
         BookingsAdapter(BookingsAdapter.BookingType.PREVIOUS)
     }


    override fun initializeUI() {
       initializeRecyclerView()
        viewModelBookings.previousBookings.reObserve(viewLifecycleOwner){
            when(it){
                is State.Success -> {
                    updateRecyclerView(it.data)
                    binding.pbBookings.hide()
                }
                is State.Loading -> {
                    binding.pbBookings.show()
                }
                is State.Error ->{

                }
            }
        }
    }

    private fun updateRecyclerView(bookings:List<Booking>?){
        if(bookings.isNullOrEmpty()){
          TODO("show no data message")
        }else{
            bookingsAdapter.setNewBookings(bookings)
        }
    }

    private fun initializeRecyclerView() {
        binding.rvBookings.apply {
            adapter = bookingsAdapter
            addItemDecoration(CustomDivider(10))
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}