package com.andrei.UI.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.andrei.carrental.R
import com.andrei.carrental.databinding.FragmentSignUpLayoutBinding
import com.andrei.carrental.viewmodels.ViewModelSignUp
import com.andrei.engine.repository.interfaces.UsernameState
import com.andrei.utils.reObserve
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_sign_up_layout){


    private val viewModelSignUp:ViewModelSignUp by viewModels ()

    private val  binding:FragmentSignUpLayoutBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeUI()
    }

    private fun initializeUI() {
        attachObservers()
        attachListenerForUsername()
    }

    private fun attachObservers() {
            viewModelSignUp.validationErrorUsername.reObserve(viewLifecycleOwner) {
                when(it){
                    is UsernameState.Valid -> {
                        binding.errorUsername = null
                        binding.tfUsername.endIconDrawable = ContextCompat.getDrawable(requireContext(),R.drawable.ic_check_24)
                    }
                    is UsernameState.AlreadyTaken -> {
                        binding.errorUsername = requireContext().getString(R.string.username_taken)
                    }
                    is UsernameState.ErrorFormat -> {
                        binding.errorUsername = requireContext().getString(R.string.username_error_format)
                    }
                }
        }
    }

    private fun attachListenerForUsername() {
        binding.apply {
            val handler = Handler(Looper.getMainLooper())
            val callback  =  Runnable{
                val text  = tfUsername.editText?.text.toString()
                    viewModelSignUp.enteredUsername.postValue(text)
                }
            tfUsername.editText?.addTextChangedListener{
                handler.executeDelayed(callback)
            }
        }
    }

    private fun Handler.executeDelayed(callback:Runnable, time : Long = 500){
       removeCallbacks(callback)
       postDelayed(callback,1000)
    }

}