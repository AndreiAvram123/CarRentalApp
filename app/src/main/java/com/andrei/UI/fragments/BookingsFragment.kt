package com.andrei.UI.fragments

import by.kirich1409.viewbindingdelegate.viewBinding
import com.andrei.carrental.R
import com.andrei.carrental.databinding.FragmentBookingsLayoutBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookingsFragment :BaseFragment(R.layout.fragment_bookings_layout){

    override val binding: FragmentBookingsLayoutBinding by viewBinding()


    override fun initializeUI() {

    }
}