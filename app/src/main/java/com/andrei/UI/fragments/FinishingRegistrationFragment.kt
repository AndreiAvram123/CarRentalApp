package com.andrei.UI.fragments

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.andrei.carrental.R
import com.andrei.carrental.databinding.FragmentRegistrationCompleteLayoutBinding
import com.andrei.carrental.viewmodels.ViewModelSignUp
import com.andrei.engine.states.RegistrationResponse
import com.andrei.utils.hide
import com.andrei.utils.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class FinishingRegistrationFragment : BaseFragment(R.layout.fragment_registration_complete_layout) {

    private val binding: FragmentRegistrationCompleteLayoutBinding by viewBinding()
    private val viewModelSignUp:ViewModelSignUp by activityViewModels()

    override fun initializeUI() {
        binding.apply {
            btGoToLogin.setOnClickListener {
                navigateToLogin()
            }
        }
        lifecycleScope.launchWhenResumed {
            viewModelSignUp.registrationState.collect {
                updateUI(it)
            }
        }

    }
    private fun updateUI(state:RegistrationResponse){
        when(state){
            is RegistrationResponse.Loading ->{
                playLoadingAnimation()
            }
            is RegistrationResponse.Complete -> {
                stopLoadingAnimation()
                playFinishedAnimation()
                showMessage(getString(R.string.registration_complete))
                enableNextButton()
            }

            is RegistrationResponse.RegistrationError ->{
                stopLoadingAnimation()
                playErrorAnimation()
                showMessage(getString(R.string.registration_error))

            }
        }
    }


    private fun showMessage(message:String) {
        binding.tvMessage.text = message
    }

    private fun playErrorAnimation() {
        binding.errorAnimation.apply {
            show()
            playAnimation()
        }
    }
    private fun stopLoadingAnimation(){
        binding.loadingAnimation.apply {
            hide()
            pauseAnimation()
        }
    }

    private fun playFinishedAnimation() {
        binding.finishedAnimation.apply {
            show()
            playAnimation()
        }
    }

    private fun playLoadingAnimation() {
        binding.loadingAnimation.apply {
            show()
            playAnimation()
        }
    }

    private fun enableNextButton(){
        binding.btGoToLogin.apply {
            isEnabled = true
            show()
        }
    }

    private fun navigateToLogin(){
        val action = FinishingRegistrationFragmentDirections.actionRegistrationCompleteFragmentToLoginFragment()
        findNavController().navigate(action)
    }


}