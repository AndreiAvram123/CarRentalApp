package com.andrei.UI.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.andrei.carrental.databinding.FragmentLoginLayoutBinding
import com.andrei.carrental.viewmodels.ViewModelAuth

class LoginFragment :Fragment() {

    private lateinit var binding:FragmentLoginLayoutBinding
    private val viewModelAuth : ViewModelAuth by activityViewModels ()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginLayoutBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = this
        binding.viewModelAuth = viewModelAuth
        attachListeners()
       return binding.root
    }

    private fun attachListeners() {
        binding.btLogin.setOnClickListener {
            viewModelAuth.usernameEntered.value = binding.tfUsername.editText.toString()
            viewModelAuth.passwordEntered.value = binding.tfPassword.editText.toString()
             viewModelAuth.startLoginFlow()
        }
    }

}