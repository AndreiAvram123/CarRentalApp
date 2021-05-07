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
import com.andrei.utils.onTextChanges
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ChooseUsernameFragment : BaseRegistrationFragment(R.layout.fragment_choose_usename_layout) {

    private val binding:FragmentChooseUsenameLayoutBinding by viewBinding()



    override fun initializeUI() {
        binding.tfUsernameEditText.onTextChanges()
            .onEach {
                hideUsernameError()
                disableNextButton()

            }
            .debounce(500)
            .onEach {
                viewModelSignUp.setUsername(it.toString())
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        lifecycleScope.launchWhenResumed {
            viewModelSignUp.validationUsername.collect{
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
        }


        binding.btNext.setOnClickListener {
            navigateForward()
        }
        binding.btBack.setOnClickListener {
            navigateBack()
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