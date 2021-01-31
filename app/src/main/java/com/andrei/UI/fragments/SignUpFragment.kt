package com.andrei.UI.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.andrei.carrental.R
import com.andrei.carrental.databinding.FragmentSignUpLayoutBinding
import com.andrei.carrental.viewmodels.ViewModelSignUp
import com.andrei.engine.repository.interfaces.UsernameState
import com.andrei.utils.reObserve
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment : Fragment(){


    private val viewModelSignUp:ViewModelSignUp by viewModels ()

    private var binding:FragmentSignUpLayoutBinding? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val newBinding =  FragmentSignUpLayoutBinding.inflate(inflater,container,false)
        binding = newBinding

        initializeUI()
        return newBinding.root
    }

    private fun initializeUI() {
        attachObservers()
        attachListenerForUsername()
    }

    private fun attachObservers() {
            viewModelSignUp.validationErrorUsername.reObserve(viewLifecycleOwner) {
                when(it){
                    is UsernameState.Valid -> binding?.errorUsername = null
                    is UsernameState.AlreadyTaken -> binding?.errorUsername = requireContext().getString(R.string.username_taken)
                    is UsernameState.ErrorFormat -> binding?.errorUsername = requireContext().getString(R.string.username_error_format)
                }
        }
    }

    private fun attachListenerForUsername() {
        binding?.apply {
            val handler = Handler(Looper.getMainLooper())
            val callback  =  Runnable{
                //update the value as fast as possible
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main){
                    viewModelSignUp.enteredUsername.value = tfUsername.editText?.text.toString()
                }
            }
            tfUsername.editText?.addTextChangedListener{
                handler.removeCallbacks(callback)
                handler.postDelayed(callback,1000)
            }


        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}