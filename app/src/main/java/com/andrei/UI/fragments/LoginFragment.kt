package com.andrei.UI.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.andrei.carrental.R
import com.andrei.carrental.databinding.FragmentLoginLayoutBinding
import com.andrei.carrental.viewmodels.ViewModelAuth
import com.andrei.utils.reObserve

class LoginFragment :Fragment(R.layout.fragment_login_layout) {

    private  val  binding:FragmentLoginLayoutBinding by viewBinding()
    private val viewModelAuth : ViewModelAuth by activityViewModels ()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeUI()
    }

    private fun attachObservers() {
        viewModelAuth.errorEmail.reObserve(viewLifecycleOwner){
         binding.errorEmail = it
        }
        viewModelAuth.errorPassword.reObserve(viewLifecycleOwner){
            binding.errorPassword = it
        }
        viewModelAuth.authenticationState.reObserve(viewLifecycleOwner) {
            binding.isAuthenticationInProgress = it == ViewModelAuth.AuthenticationState.AUTHENTICATING
        }
    }

    private fun initializeUI() {
        attachObservers()
        attachViewListeners()
    }

    private fun attachViewListeners() {
        binding.apply {
            btLogin.setOnClickListener {
            viewModelAuth.emailEntered.value = binding.tfEmail.editText?.text.toString()
            viewModelAuth.passwordEntered.value = binding.tfPassword.editText?.text.toString()
            viewModelAuth.startLoginFlow()
        }
            tfRegister.setOnClickListener {
                val action = LoginFragmentDirections.actionLoginFragmentToSignUpFragment()
                findNavController().navigate(action)
            }

        }


    }

}