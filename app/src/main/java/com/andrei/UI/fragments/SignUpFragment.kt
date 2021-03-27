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
import com.andrei.engine.repository.interfaces.EmailValidationState
import com.andrei.engine.repository.interfaces.PasswordValidationState
import com.andrei.engine.repository.interfaces.UsernameValidationState
import com.andrei.engine.states.RegistrationFlowState
import com.andrei.utils.parseText
import com.andrei.utils.reObserve
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : BaseFragment(R.layout.fragment_sign_up_layout){


    private val _viewModelSignUp:ViewModelSignUp by viewModels ()

    private val  binding:FragmentSignUpLayoutBinding by viewBinding()

    private val runnableUsername  =  Runnable{
        val text  = binding.tfUsername.editText.parseText()
        _viewModelSignUp.setUsername(text)
    }
    private val runnableEmail = Runnable {
        val text = binding.tfEmail.editText.parseText()
        _viewModelSignUp.setEmail(text)
    }

    private val runnablePassword  =  Runnable{
        val text  = binding.tfPassword.editText.parseText()
        _viewModelSignUp.setPassword(text)
    }
    private val runnableReenterPassword = Runnable {
      val confirmedPassword = binding.tfReenterPassword.editText.parseText()
      _viewModelSignUp.setReenteredPassword(confirmedPassword)
    }


    private val observerValidationStateUsername = Observer<UsernameValidationState>{
        when(it){
            is UsernameValidationState.Valid -> {
                binding.errorUsername = null
            }
            is UsernameValidationState.AlreadyTaken -> {
                binding.errorUsername = requireContext().getString(R.string.username_taken)
            }
            is UsernameValidationState.TooShort -> {
                binding.errorUsername = requireContext().getString(R.string.username_too_short)
            }
        }
    }
    private val observerValidationStatePassword = Observer<PasswordValidationState>{
        when(it){
            is PasswordValidationState.Valid -> {
                binding.errorPassword = null
            }
            is PasswordValidationState.TooWeak -> {
                binding.errorPassword = getString(R.string.password_too_weak)
            }
        }
    }


    private val observerConfirmedPasswordValid = Observer<Boolean> {valid->
        if(valid){
            binding.errorConfirmedPassword = null
        }else{
            binding.errorConfirmedPassword = getString(R.string.passwords_no_match)
        }
    }


    private val observerRegistrationFlow = Observer<RegistrationFlowState> {
        if(it is RegistrationFlowState.Finished){
            navigateToRegistrationCompleteFragment()
        }
    }



    private val handler = Handler(Looper.getMainLooper())


    override fun initializeUI() {
        binding.viewModelSignUp = _viewModelSignUp
        binding.lifecycleOwner = viewLifecycleOwner
        attachObservers()
        attachListeners()

    }

    private fun attachListeners() {
        binding.apply {
            btBack.setOnClickListener{findNavController().popBackStack()}
            btRegister.setOnClickListener {
               // _viewModelSignUp.startRegistrationFlow()
            }
            tfUsername.editText?.addTextChangedListener{
                handler.executeDelayed(runnableUsername)
            }
            tfPassword.editText?.addTextChangedListener {
                handler.executeDelayed(runnablePassword)
            }
            tfEmail.editText?.addTextChangedListener {
                handler.executeDelayed(runnableEmail)
            }

            tfReenterPassword.editText?.addTextChangedListener {
                handler.executeDelayed(runnableReenterPassword)
            }
        }

    }
    private fun navigateToRegistrationCompleteFragment(){

    }

    private fun attachObservers() {
      //   _viewModelSignUp.validationStateUsername.reObserve(viewLifecycleOwner,observerValidationStateUsername)
      //   _viewModelSignUp.validationStatePassword.reObserve(viewLifecycleOwner,observerValidationStatePassword)
     //   _viewModelSignUp.reenteredPasswordValid.reObserve(viewLifecycleOwner,observerConfirmedPasswordValid)
     //   _viewModelSignUp.registrationState.reObserve(viewLifecycleOwner,observerRegistrationFlow)
    }

    private fun Handler.executeDelayed(callback:Runnable){
        removeCallbacks(callback)
        postDelayed(callback,1200)
    }

}