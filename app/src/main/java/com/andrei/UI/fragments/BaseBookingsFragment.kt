package com.andrei.UI.fragments

import by.kirich1409.viewbindingdelegate.viewBinding
import com.andrei.carrental.R
import com.andrei.carrental.databinding.FragmentBaseBookingsLayoutBinding

abstract class BaseBookingsFragment : BaseFragment(R.layout.fragment_base_bookings_layout) {
   internal val binding:FragmentBaseBookingsLayoutBinding by viewBinding()
}