package com.andrei.UI.fragments.registration

import android.os.Handler
import android.os.Looper
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import by.kirich1409.viewbindingdelegate.viewBinding
import com.andrei.UI.fragments.BaseFragment
import com.andrei.carrental.R
import com.andrei.carrental.databinding.FragmentChooseUsenameLayoutBinding
import com.andrei.carrental.viewmodels.ViewModelSignUp
import com.andrei.engine.repository.interfaces.UsernameValidationState
import com.andrei.utils.executeDelayed
import com.andrei.utils.parseText
import com.andrei.utils.reObserve
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChooseUsernameFragment : BaseFragment(R.layout.fragment_choose_usename_layout) {

    private val handler = Handler(Looper.getMainLooper())
    private val binding:FragmentChooseUsenameLayoutBinding by viewBinding()
    private val viewModelSignUp: ViewModelSignUp by activityViewModels ()

    private val runnableUsername  =  Runnable{
        val text  = binding.tfUsername.editText.parseText()
        viewModelSignUp.setUsername(text)
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

    override fun initializeUI() {
        binding.tfUsername.editText?.addTextChangedListener {
            handler.executeDelayed(runnableUsername)
        }
        viewModelSignUp.validationStateUsername.reObserve(this, observerValidationStateUsername)
    }
}