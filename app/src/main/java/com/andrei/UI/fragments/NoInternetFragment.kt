package com.andrei.UI.fragments

import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.andrei.carrental.R
import com.andrei.carrental.databinding.FragmentNoInternetLayoutBinding

class NoInternetFragment : BaseFragment(R.layout.fragment_no_internet_layout) {

    val binding: FragmentNoInternetLayoutBinding by viewBinding()

    override fun initializeUI() {
        binding.apply{
            btRetry.setOnClickListener {
                findNavController().popBackStack()
            }
        }

    }



}