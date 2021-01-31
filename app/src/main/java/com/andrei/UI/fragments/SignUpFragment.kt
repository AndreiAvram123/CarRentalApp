package com.andrei.UI.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.andrei.carrental.databinding.FragmentSignUpLayoutBinding
import com.andrei.carrental.viewmodels.ViewModelSignUp
import com.andrei.utils.reObserve
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment(){


    private var binding:FragmentSignUpLayoutBinding?= null
    private val viewModelSignUp:ViewModelSignUp by viewModels ()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val tempBinding = FragmentSignUpLayoutBinding.inflate(inflater,container,false)
        binding = tempBinding
        viewModelSignUp.validationErrorUsername.reObserve(viewLifecycleOwner){
            binding?.errorUsername = it
        }
        attachFieldListeners()
        return tempBinding.root
    }

    private fun attachFieldListeners() {

        binding?.apply {
            val handler = Handler(Looper.getMainLooper())

            val callback  =  Runnable{
                viewModelSignUp.enteredUsername.postValue(tfUsername.editText?.text.toString())
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