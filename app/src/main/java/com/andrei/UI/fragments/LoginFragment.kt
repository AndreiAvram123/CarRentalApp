package com.andrei.UI.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.andrei.UI.FieldValidation
import com.andrei.carrental.R
import com.andrei.carrental.databinding.FragmentLoginLayoutBinding
import com.andrei.carrental.viewmodels.ViewModelLogin
import com.andrei.engine.states.LoginFlowState
import com.andrei.utils.parseText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class LoginFragment :Fragment(R.layout.fragment_login_layout) {

    private  val  binding:FragmentLoginLayoutBinding by viewBinding()
    private val viewModelLogin : ViewModelLogin by activityViewModels ()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeUI()
    }

    private fun attachObservers() {
        lifecycleScope.launchWhenResumed {
            viewModelLogin.errorEmail.collect {
                when (it) {
                    is FieldValidation.Invalid -> binding.errorEmail = it.error
                    else -> binding.errorEmail = null
                }
            }
        }
        lifecycleScope.launchWhenResumed {
            viewModelLogin.errorPassword.collect {
                when (it) {
                    is FieldValidation.Invalid -> binding.errorPassword = it.error
                    else -> binding.errorPassword = null
                }
            }
        }

        lifecycleScope.launchWhenResumed {
            viewModelLogin.loginFlowState.collect {
                binding.isAuthenticationInProgress = it == LoginFlowState.Loading
            }
        }
    }

    private fun initializeUI() {
        attachObservers()
        attachViewListeners()
    }

    private fun attachViewListeners() {
        binding.apply {
            btLogin.setOnClickListener {
                val password = binding.tfPassword.editText.parseText()
                val email = binding.tfEmail.editText.parseText()
                viewModelLogin.login(email,password)
        }
            tfRegister.setOnClickListener {
                val action = LoginFragmentDirections.actionLoginFragmentToChooseUsernameFragment()
                findNavController().navigate(action)
            }

        }

    }

}