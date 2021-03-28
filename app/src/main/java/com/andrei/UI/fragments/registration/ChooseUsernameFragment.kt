package com.andrei.UI.fragments.registration

import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.andrei.carrental.R
import com.andrei.carrental.databinding.FragmentChooseUsenameLayoutBinding
import com.andrei.carrental.viewmodels.ViewModelSignUp
import com.andrei.engine.State
import com.andrei.engine.repository.interfaces.UsernameValidationState
import com.andrei.utils.executeDelayed
import com.andrei.utils.handler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ChooseUsernameFragment : BaseRegistrationFragment(R.layout.fragment_choose_usename_layout) {

    private val binding:FragmentChooseUsenameLayoutBinding by viewBinding()

    override val runnableDetail  =  Runnable{
        viewModelSignUp.validateUsername()
    }


    private val collectorUsernameValidation : suspend (State<UsernameValidationState>)-> Unit = {
        when (it) {
            is State.Success -> {
                binding.validationInProgress = false

                when (it.data) {
                    is UsernameValidationState.AlreadyTaken -> {
                        showError(getString(R.string.username_taken))
                    }
                    is UsernameValidationState.TooShort ->
                        showError(getString(R.string.username_too_short))

                    is UsernameValidationState.Valid -> {
                        hideUsernameError()
                        enableNextButton()
                    }
                    is UsernameValidationState.Unvalidated -> {
                        hideUsernameError()
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
        binding.tfUsername.editText?.addTextChangedListener {
            hideUsernameError()
            disableNextButton()
            viewModelSignUp.setUsername(it.toString())
            handler.executeDelayed(runnableDetail)
        }

        lifecycleScope.launchWhenResumed {
            viewModelSignUp.validationUsername.collect(collectorUsernameValidation)
        }


        binding.btNext.setOnClickListener {
            navigateForward()
        }
    }




    private fun hideUsernameError(){
        binding.errorUsername = null
    }

    override fun disableNextButton(){
        binding.btNext.isEnabled = false
    }
    override fun enableNextButton(){
        binding.btNext.isEnabled = true
    }
    override fun showError(error: String) {
        binding.errorUsername = error
    }

    override fun hideError() {
        binding.errorUsername = null
    }

    override fun navigateForward() {
        val action = ChooseUsernameFragmentDirections.actionChooseUsernameFragmentToChooseEmailFragment()
        findNavController().navigate(action)
    }
}