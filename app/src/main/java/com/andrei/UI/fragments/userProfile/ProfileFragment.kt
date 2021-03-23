package com.andrei.UI.fragments.userProfile

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.andrei.UI.fragments.BaseFragment
import com.andrei.carrental.R
import com.andrei.carrental.databinding.FragmentProfileBinding
import com.andrei.carrental.viewmodels.ViewModelUser
import com.andrei.engine.State
import kotlinx.coroutines.flow.collect


class ProfileFragment : BaseFragment(R.layout.fragment_profile) {

   private val binding:FragmentProfileBinding by viewBinding()
   private val navArgs:ProfileFragmentArgs by navArgs()
   private val viewModelUser:ViewModelUser by viewModels()

    override fun initializeUI() {
        lifecycleScope.launchWhenResumed {
            viewModelUser.getUser(navArgs.userID)
            viewModelUser.currentUser.collect {
                when(it){
                     is State.Success -> binding.user = it.data
                }
            }
        }
    }

}