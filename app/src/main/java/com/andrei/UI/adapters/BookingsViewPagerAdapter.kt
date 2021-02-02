package com.andrei.UI.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.andrei.UI.fragments.UpcomingBookingsFragment

class BookingsViewPagerAdapter(fa:FragmentActivity) : FragmentStateAdapter(fa){

    val fragments:List<Fragment> = listOf(UpcomingBookingsFragment(),UpcomingBookingsFragment())

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
       return  fragments[position]
    }

}