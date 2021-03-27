package com.andrei.UI.fragments.registration

import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.andrei.carrental.R
import com.andrei.carrental.databinding.FragmentChooseEmailLayoutBinding
import com.andrei.carrental.viewmodels.ViewModelSignUp
import com.andrei.engine.State
import com.andrei.engine.repository.interfaces.EmailValidationState
import com.andrei.engine.repository.interfaces.UsernameValidationState
import com.andrei.utils.executeDelayed
import com.andrei.utils.handler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ChooseEmailFragment :BaseRegistrationFragment(R.layout.fragment_choose_email_layout){

    private val binding:FragmentChooseEmailLayoutBinding by viewBinding()
    private val viewModelSignUp:ViewModelSignUp by activityViewModels()


    override val runnableDetail  =  Runnable{
        viewModelSignUp.validateEmail()
    }


    private val collectorEmailValidation : suspend (State<EmailValidationState>)-> Unit = {
        when (it) {
            is State.Success -> {
                binding.validationInProgress = false

                when (it.data) {
                    is EmailValidationState.AlreadyTaken -> {
                        showError(getString(R.string.email_already_taken))
                    }
                    is EmailValidationState.InvalidFormat->
                        showError(getString(R.string.email_format_invalid))

                    is EmailValidationState.Valid -> {
                        hideError()
                        enableNextButton()
                    }
                    is EmailValidationState.Unvalidated -> {
                        hideError()
                        disableNextButton()
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
            viewModelSignUp.setUsername(it.toString())
            handler.executeDelayed(runnableDetail)
        }

        lifecycleScope.launchWhenResumed {
            viewModelSignUp.validationEmail.collect(collectorEmailValidation)
        }


        binding.btNext.setOnClickListener {
           navigateForward()
        }
    }



    override fun showError(error: String) {
        binding.apply {
            errorEmail = error
            btNext.isEnabled = false
        }
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

    }
}