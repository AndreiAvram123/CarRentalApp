package com.andrei.UI.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.andrei.carrental.databinding.FragmentLoginLayoutBinding
import com.andrei.carrental.viewmodels.ViewModelLogin
import com.andrei.utils.reObserve

class LoginFragment :Fragment() {

    private lateinit var binding:FragmentLoginLayoutBinding
    private val viewModelLogin : ViewModelLogin by activityViewModels ()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginLayoutBinding.inflate(inflater,container,false)
        initializeUI()
        attachObservers()


       return binding.root
    }

    private fun attachObservers() {
        viewModelLogin.errorEmail.reObserve(viewLifecycleOwner){
         binding.errorEmail = it
        }
        viewModelLogin.errorPassword.reObserve(viewLifecycleOwner){
            binding.errorPassword = it
        }
        viewModelLogin.isAuthenticationInProgress.reObserve(viewLifecycleOwner){
           binding.isAuthenticationInProgress = it
        }

    }

    private fun initializeUI() {
        attachViewListeners()
    }

    private fun attachViewListeners() {
        binding.btLogin.setOnClickListener {
            viewModelLogin.emailEntered.value = binding.tfEmail.editText?.text.toString()
            viewModelLogin.passwordEntered.value = binding.tfPassword.editText?.text.toString()
            viewModelLogin.startLoginFlow()
        }
    }

}