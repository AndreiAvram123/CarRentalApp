package com.andrei.UI.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.andrei.carrental.R
import com.andrei.carrental.databinding.FragmentLoginLayoutBinding

class LoginFragment :Fragment() {

    private lateinit var binding:FragmentLoginLayoutBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginLayoutBinding.inflate(inflater,container,false)
        binding.errorUsername = "puuuuu"
       return binding.root
    }
}