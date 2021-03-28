package com.andrei.UI.fragments.registration

import android.os.Handler
import android.os.Looper
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.andrei.UI.fragments.BaseFragment
import com.andrei.carrental.R
import com.andrei.carrental.databinding.FragmentChoosePasswordBinding
import com.andrei.carrental.viewmodels.ViewModelSignUp
import com.andrei.engine.State
import com.andrei.engine.repository.interfaces.PasswordValidationState
import com.andrei.utils.executeDelayed
import com.andrei.utils.handler
import com.andrei.utils.parseText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import timber.log.Timber

@AndroidEntryPoint
class ChoosePasswordFragment : BaseFragment(R.layout.fragment_choose_password) {

    private val binding:FragmentChoosePasswordBinding by viewBinding ()
    private val viewModelSignUp:ViewModelSignUp by activityViewModels()

     private val runnablePassword: Runnable = Runnable{
       viewModelSignUp.validatePassword()
    }

    private val runnableReenteredPassword = Runnable {
         if(binding.tfPassword.editText.parseText() == binding.tfReenterPassword.editText.parseText()){
             enableNextButton()
         }else{
             showReenterPasswordError()
         }
    }

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

    private fun attachListeners() {
        binding.tfPassword.editText?.addTextChangedListener {
            hideReenteredPasswordError()
            disableNextButton()
            viewModelSignUp.setPassword(it.toString())
            handler.executeDelayed(runnablePassword)

        }
        binding.tfReenterPassword.editText?.addTextChangedListener {
            hideReenteredPasswordError()
            disableNextButton()
            viewModelSignUp.setReenteredPassword(it.toString())
            handler.executeDelayed(runnableReenteredPassword)
        }
        binding.btNext.setOnClickListener {
            navigateForward()
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

     private fun disableNextButton() {
       binding.btNext.isEnabled = false
    }

     private fun enableNextButton() {
         binding.btNext.isEnabled = true
     }

    private fun navigateForward(){

    }


}