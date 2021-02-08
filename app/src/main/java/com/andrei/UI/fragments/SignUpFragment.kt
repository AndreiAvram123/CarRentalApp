package com.andrei.UI.fragments

import android.os.Handler
import android.os.Looper
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.andrei.carrental.R
import com.andrei.carrental.databinding.FragmentSignUpLayoutBinding
import com.andrei.carrental.viewmodels.ViewModelSignUp
import com.andrei.engine.repository.interfaces.PasswordValidationState
import com.andrei.engine.repository.interfaces.UsernameValidationState
import com.andrei.utils.reObserve
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : BaseFragment(R.layout.fragment_sign_up_layout){


    private val viewModelSignUp:ViewModelSignUp by viewModels ()

    private val  binding:FragmentSignUpLayoutBinding by viewBinding()

    private val runnableUsername  =  Runnable{
        val text  = binding.tfUsername.editText?.text.toString()
        viewModelSignUp.setUsername(text)
    }

    private val runnablePassword  =  Runnable{
        val text  = binding.tfUsername.editText?.text.toString()
        viewModelSignUp.setPassword(text)
    }

    private val observerValidationStateUsername = Observer<UsernameValidationState>{
        when(it){
            is UsernameValidationState.Valid -> {
                binding.errorUsername = null
            }
            is UsernameValidationState.AlreadyTaken -> {
                binding.errorUsername = requireContext().getString(R.string.username_taken)
            }
            is UsernameValidationState.ErrorFormat -> {
                binding.errorUsername = requireContext().getString(R.string.username_error_format)
            }
        }
    }
    private val observerValidationStatePassword = Observer<PasswordValidationState>{
        when(it){
            is PasswordValidationState.Valid -> {
                binding.errorPassword = null
            }
            is PasswordValidationState.TooShort -> {
                binding.errorPassword = requireContext().getString(R.string.password_not_long_enough)
            }
            is PasswordValidationState.TooWeak -> {
                binding.errorPassword = requireContext().getString(R.string.password_too_weak)
            }
        }
    }


    private val handler = Handler(Looper.getMainLooper())


    override fun initializeUI() {
        attachObservers()
        attachListeners()
    }

    private fun attachListeners() {
        binding.btBack.setOnClickListener { findNavController().popBackStack() }
        attachListenerForUsername()
    }

    private fun attachObservers() {
         viewModelSignUp.validationStateUsername.reObserve(viewLifecycleOwner,observerValidationStateUsername)
         viewModelSignUp.validationStatePassword.reObserve(viewLifecycleOwner,observerValidationStatePassword)
    }

    private fun attachListenerForUsername() {
        binding.apply {
            tfUsername.editText?.addTextChangedListener{
                handler.executeDelayed(runnableUsername)
            }
            tfPassword.editText?.addTextChangedListener {
               handler.executeDelayed(runnablePassword)
            }


        }
    }
    private fun Handler.executeDelayed(callback:Runnable){
        removeCallbacksAndMessages(callback)
        postDelayed(callback,1000)
    }



}