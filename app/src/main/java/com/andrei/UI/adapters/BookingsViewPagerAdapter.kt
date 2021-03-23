package com.andrei.UI.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.andrei.UI.fragments.BookingsFragment
import com.andrei.carrental.entities.BookingType

class BookingsViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment){
    val fragments:List<Fragment> = listOf(BookingsFragment(BookingType.UPCOMING),
            BookingsFragment(BookingType.CURRENT),
            BookingsFragment(BookingType.PREVIOUS))

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
       return fragments[position]
    }

}