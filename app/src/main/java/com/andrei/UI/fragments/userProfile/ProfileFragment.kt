package com.andrei.UI.fragments.userProfile

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.andrei.UI.fragments.BaseFragment
import com.andrei.carrental.R
import com.andrei.carrental.UserDataManager
import com.andrei.carrental.databinding.FragmentProfileBinding
import com.andrei.carrental.viewmodels.ViewModelUser
import com.andrei.engine.State
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : BaseFragment(R.layout.fragment_profile) {

   private val binding:FragmentProfileBinding by viewBinding()
   private val navArgs:ProfileFragmentArgs by navArgs()
   private val viewModelUser:ViewModelUser by viewModels()
   private val viewModelProfile:ViewModelProfile by viewModels()

    @Inject
    lateinit var userDataManager: UserDataManager


    override fun initializeUI() {
        lifecycleScope.launchWhenResumed {
            viewModelUser.getUser(navArgs.userID)
            viewModelUser.currentUser.collect {
                when(it){
                     is State.Success -> binding.user = it.data
                }
            }
        }
        attachListeners()
    }


    private fun attachListeners() {
        binding.btSendMessage.setOnClickListener {
            viewModelProfile.getUsersChat(userDataManager.userID,navArgs.userID)

            lifecycleScope.launchWhenResumed {
                viewModelProfile.usersChat.collect {
                    if(it is State.Success){
                        navigateToMessagesFragment(it.data.id)
                    }
                }
            }
        }
    }

    private fun navigateToMessagesFragment(chatID:Long){
        val action = ProfileFragmentDirections.actionProfileFragmentToMessagesFragment2(chatID)
        findNavController().navigate(action)
    }

}