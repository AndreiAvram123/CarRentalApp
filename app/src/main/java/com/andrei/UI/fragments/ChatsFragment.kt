package com.andrei.UI.fragments

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.andrei.UI.adapters.CustomDivider
import com.andrei.UI.adapters.chats.ChatsAdapter
import com.andrei.carrental.R
import com.andrei.carrental.UserDataManager
import com.andrei.carrental.databinding.FragmentChatsBinding
import com.andrei.carrental.viewmodels.ViewModelChat
import com.andrei.engine.State
import com.andrei.messenger.MessengerService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class ChatsFragment: BaseFragment(R.layout.fragment_chats) {


    @Inject
    lateinit var messengerService: MessengerService
    @Inject
    lateinit var userDataManager: UserDataManager

    private val binding:FragmentChatsBinding by viewBinding()
    private val viewModelChat:ViewModelChat by activityViewModels()

    private val chatsAdapter by lazy {
        ChatsAdapter(viewLifecycleOwner,this::goToMessagesFragment)
    }


    override fun initializeUI() {
         initializeRecyclerView()
         initializeObservers()


    }

    private fun initializeObservers() {
        lifecycleScope.launchWhenResumed {
           viewModelChat.getUserChats(userDataManager.userID)
           viewModelChat.userChats.collect {
               when(it){
                   is State.Success -> {
                       chatsAdapter.setData(messengerService.getObservableChats())
                   }
               }
           }
           }

        }


    private fun goToMessagesFragment(chatID:Long){
        val action = ChatsFragmentDirections.actionChatsFragmentToMessagesFragment(chatID)
        findNavController().navigate(action)
    }

    private fun initializeRecyclerView() {
        binding.rvChats.apply {
            adapter = chatsAdapter
            addItemDecoration(CustomDivider(10))
            layoutManager = LinearLayoutManager(context)
        }
    }

}