package com.andrei.UI.fragments

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.andrei.UI.adapters.CustomDivider
import com.andrei.UI.adapters.chats.ChatsAdapter
import com.andrei.carrental.R
import com.andrei.carrental.databinding.FragmentChatsBinding
import com.andrei.carrental.viewmodels.ViewModelChat
import com.andrei.engine.State
import com.andrei.services.MessengerService
import com.andrei.utils.reObserve
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChatsFragment: BaseFragment(R.layout.fragment_chats) {


    @Inject
    lateinit var messengerService: MessengerService

    private val binding:FragmentChatsBinding by viewBinding()
    private val viewModelChat:ViewModelChat by viewModels()

    private val chatsAdapter:ChatsAdapter by lazy {
        ChatsAdapter(viewLifecycleOwner, navigateToMessagesCallback = this::goToMessagesFragment)
    }

    override fun initializeUI() {
         initializeRecyclerView()
         viewModelChat.userChats.reObserve(viewLifecycleOwner){
             when(it){
                 is State.Success -> {
                     if(it.data !=null){
                         messengerService.configureChannels(it.data)
                         messengerService.connect()
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