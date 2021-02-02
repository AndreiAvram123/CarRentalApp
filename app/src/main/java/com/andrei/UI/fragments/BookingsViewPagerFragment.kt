package com.andrei.UI.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.andrei.UI.adapters.BookingsViewPagerAdapter
import com.andrei.carrental.R
import com.andrei.carrental.databinding.FragmentBookingsViewPagerLayoutBinding
import com.google.android.material.tabs.TabLayoutMediator

class BookingsViewPagerFragment : BaseFragment(R.layout.fragment_bookings_view_pager_layout) {

    val binding: FragmentBookingsViewPagerLayoutBinding by viewBinding()

    override fun initializeUI() {
        binding.vp.apply {
            adapter = BookingsViewPagerAdapter(requireActivity())
        }
        TabLayoutMediator(binding.tabLayout,binding.vp){ tab,position ->
            tab.text = when(position){
             1-> "Upcoming"
             2-> "Current"
             else ->"Previous"
            }
        }.attach()
    }

}