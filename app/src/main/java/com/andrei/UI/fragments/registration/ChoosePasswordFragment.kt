package com.andrei.UI.fragments.registration

import android.os.Handler
import android.os.Looper
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.andrei.UI.fragments.BaseFragment
import com.andrei.carrental.R
import com.andrei.carrental.databinding.FragmentChoosePasswordBinding
import com.andrei.carrental.viewmodels.ViewModelSignUp
import com.andrei.engine.State
import com.andrei.engine.repository.interfaces.PasswordValidationState
import com.andrei.utils.onTextChanges
import com.andrei.utils.parseText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

@AndroidEntryPoint
class ChoosePasswordFragment : BaseRegistrationFragment(R.layout.fragment_choose_password) {

    private val binding:FragmentChoosePasswordBinding by viewBinding ()

    override fun initializeUI() {
        lifecycleScope.launchWhenResumed {
            viewModelSignUp.validationPassword.collect {
                when (it) {
                    is State.Success -> {
                        binding.validationInProgress = false
                        when (it.data) {
                            is PasswordValidationState.TooWeak -> {
                                showPasswordError(getString(R.string.password_too_weak))
                            }
                            is PasswordValidationState.Valid -> {
                                hidePasswordError()
                                enableNextButton()
                            }

                            is PasswordValidationState.Unvalidated -> {
                                hidePasswordError()
                                disableNextButton()
                            }
                        }
                    }
                    is State.Error -> {
                        binding.validationInProgress = false
                    }
                    else -> binding.validationInProgress = true
                }
            }
        }


         attachListeners()
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    private fun attachListeners() {

        binding.tfPasswordEditText.onTextChanges().onEach {
            hideReenteredPasswordError()
            disableNextButton()
            }
            .debounce(500)
            .onEach {
                viewModelSignUp.setPassword(it.toString())
            }

            .launchIn(viewLifecycleOwner.lifecycleScope)

        binding.tfReenterPasswordEditText.onTextChanges().onEach {
            hideReenteredPasswordError()
            disableNextButton()
        }.debounce(500)
            .onEach {
                if(binding.tfPassword.editText.parseText() == it.toString()){
                    enableNextButton()
                }else{
                    showReenterPasswordError()
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)


        binding.btNext.setOnClickListener {
            navigateForward()
        }
        binding.btBack.setOnClickListener {
            navigateBack()
        }
    }

    private fun hideReenteredPasswordError() {
        binding.tfReenterPassword.apply {
            error = null
            isErrorEnabled = false
        }
    }
    private fun showReenterPasswordError() {
        binding.tfReenterPassword.apply {
             error = getString(R.string.passwords_no_match)
             isErrorEnabled = true
        }
    }


    private fun hidePasswordError(){
        binding.errorPassword = null
    }

    private fun showPasswordError(error: String) {
        binding.errorPassword = error
    }

    override fun showError(error: String) {

    }

    override fun hideError() {
    }

    override fun disableNextButton() {
       binding.btNext.isEnabled = false
    }

     override fun enableNextButton() {
         binding.btNext.isEnabled = true
     }

    override fun navigateForward(){
       val action = ChoosePasswordFragmentDirections.actionChoosePasswordFragmentToChooseProfilePictureFragment()
        findNavController().navigate(action)

    }


}