package com.andrei.UI.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.andrei.carrental.R
import com.andrei.carrental.databinding.FragmentNoInternetLayoutBinding

class NoInternetFragment : BaseFragment() {
    private  var binding: FragmentNoInternetLayoutBinding? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNoInternetLayoutBinding.inflate(inflater,container,false)
        binding?.apply{
            btRetry.setOnClickListener {
                findNavController().popBackStack()
            }
        }
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}