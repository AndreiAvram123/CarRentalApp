package com.andrei.UI.fragments

import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.andrei.carrental.R
import com.andrei.carrental.databinding.FragmentRegistrationCompleteLayoutBinding
import com.andrei.carrental.databinding.FragmentRegistrationCompleteLayoutBindingImpl

class RegistrationCompleteFragment : BaseFragment(R.layout.fragment_registration_complete_layout) {


    private val binding: FragmentRegistrationCompleteLayoutBinding by viewBinding()

    override fun initializeUI() {
       binding.apply {
           btGoToLogin.setOnClickListener {
               navigateToLogin()
           }
       }
    }
    private fun navigateToLogin(){
        val action = RegistrationCompleteFragmentDirections.actionRegistrationCompleteFragmentToLoginFragment()
        findNavController().navigate(action)
    }


}