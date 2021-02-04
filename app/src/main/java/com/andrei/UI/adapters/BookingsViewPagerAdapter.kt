package com.andrei.UI.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.andrei.UI.fragments.BookingsFragment

class BookingsViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment){
    val fragments:List<Fragment> = listOf(BookingsFragment(BookingsFragment.BookingType.UPCOMING),
            BookingsFragment(BookingsFragment.BookingType.CURRENT),
            BookingsFragment(BookingsFragment.BookingType.PREVIOUS))

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
       return fragments[position]
    }

}