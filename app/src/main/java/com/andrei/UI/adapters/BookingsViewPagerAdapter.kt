package com.andrei.UI.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.andrei.UI.fragments.BookingsFragment

class BookingsViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment){


    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
       return  BookingsFragment()
    }

}