package com.andrei.UI.fragments.registration

import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.andrei.carrental.R
import com.andrei.carrental.databinding.FragmentChooseEmailLayoutBinding
import com.andrei.engine.State
import com.andrei.engine.repository.interfaces.EmailValidationState
import com.andrei.utils.executeDelayed
import com.andrei.utils.handler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ChooseEmailFragment :BaseRegistrationFragment(R.layout.fragment_choose_email_layout){

    private val binding:FragmentChooseEmailLayoutBinding by viewBinding()



    private val collectorEmailValidation : suspend (State<EmailValidationState>)-> Unit = {
        when (it) {
            is State.Success -> {
                binding.validationInProgress = false
                when (it.data) {
                    is EmailValidationState.AlreadyTaken -> {
                        showError(getString(R.string.email_already_taken))
                    }
                    is EmailValidationState.InvalidFormat-> {
                        showError(getString(R.string.email_format_invalid))
                    }

                    is EmailValidationState.Valid -> {
                        hideError()
                        enableNextButton()
                    }
                    is EmailValidationState.Unvalidated -> {
                        hideError()
                    }
                }
            }
            is State.Error -> {
                binding.validationInProgress = false
            }
            else ->  binding.validationInProgress = true
        }
    }


    override fun initializeUI() {
        binding.tfEmail.editText?.addTextChangedListener {
            hideError()
            disableNextButton()
            viewModelSignUp.setEmail(it.toString())
            handler.executeDelayed{
                viewModelSignUp.validateEmail()
            }
        }

        lifecycleScope.launchWhenResumed {
            viewModelSignUp.validationEmail.collect(collectorEmailValidation)
        }


        binding.btNext.setOnClickListener {
           navigateForward()
        }
        binding.btBack.setOnClickListener {
            navigateBack()
        }

    }



    override fun showError(error: String) {
            binding.errorEmail = error
    }

    override fun hideError() {
        binding.errorEmail = null
    }

    override fun disableNextButton(){
        binding.btNext.isEnabled = false
    }
    override fun enableNextButton(){
        binding.btNext.isEnabled = true
    }

    override fun navigateForward() {
        val action = ChooseEmailFragmentDirections.actionChooseEmailFragmentToChoosePasswordFragment()
        findNavController().navigate(action)
    }

}