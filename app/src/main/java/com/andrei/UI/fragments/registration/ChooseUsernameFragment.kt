package com.andrei.UI.fragments.registration

import android.os.Handler
import android.os.Looper
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.andrei.UI.fragments.BaseFragment
import com.andrei.carrental.R
import com.andrei.carrental.databinding.FragmentChooseUsenameLayoutBinding
import com.andrei.carrental.viewmodels.ViewModelSignUp
import com.andrei.engine.State
import com.andrei.engine.repository.interfaces.UsernameValidationState
import com.andrei.utils.executeDelayed
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ChooseUsernameFragment : BaseFragment(R.layout.fragment_choose_usename_layout) {

    private val handler = Handler(Looper.getMainLooper())
    private val binding:FragmentChooseUsenameLayoutBinding by viewBinding()
    private val viewModelSignUp: ViewModelSignUp by activityViewModels ()

    private val runnableValidation  =  Runnable{
        viewModelSignUp.validateUsername()
    }

    private val collectorUsernameValidation : suspend (State<UsernameValidationState>)-> Unit = {
        when (it) {
            is State.Success -> {
                binding.validationInProgress = false

                when (it.data) {
                    is UsernameValidationState.AlreadyTaken -> {
                        showUsernameError(getString(R.string.username_taken))
                    }
                    is UsernameValidationState.TooShort ->
                        showUsernameError(getString(R.string.username_too_short))

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
            handler.executeDelayed(runnableValidation)
        }

        lifecycleScope.launchWhenResumed {
            viewModelSignUp.validationUsername.collect(collectorUsernameValidation)
        }


        binding.btNext.setOnClickListener {

        }
    }




    private fun showUsernameError(errorText:String){
        binding.apply {
            errorUsername = errorText
            btNext.isEnabled = false
        }
    }

    private fun hideUsernameError(){
        binding.errorUsername = null
    }

    private fun disableNextButton(){
        binding.btNext.isEnabled = false
    }
    private fun enableNextButton(){
        binding.btNext.isEnabled = true
    }
}