package com.andrei.UI.fragments

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.andrei.UI.adapters.CustomDivider
import com.andrei.UI.adapters.chats.ChatsAdapter
import com.andrei.carrental.R
import com.andrei.carrental.databinding.FragmentChatsBinding
import com.andrei.carrental.entities.Chat
import com.andrei.carrental.entities.User
import com.andrei.carrental.viewmodels.ViewModelBookings
import com.andrei.carrental.viewmodels.ViewModelLocation
import com.andrei.services.MessengerService
import com.andrei.utils.reObserve
import com.pusher.client.Pusher
import com.pusher.client.PusherOptions
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChatsFragment: BaseFragment(R.layout.fragment_chats) {

    @Inject
    lateinit var pusherOptions: PusherOptions

    private val binding:FragmentChatsBinding by viewBinding()

    private val chatsAdapter:ChatsAdapter by lazy {
        ChatsAdapter(viewLifecycleOwner)
    }

    override fun initializeUI() {
         initializeRecyclerView()
            val messengerService = MessengerService(usersIDs = listOf(1),requireContext(),pusherOptions)
            messengerService.connect()

            val mockChat = Chat(User(1,"andrei"),
                isUserOnline= messengerService.getUserOnlineLiveData(1),
                lastMessage = messengerService.getLastMessageLiveData(1))

            chatsAdapter.setData(listOf(mockChat))
    }

    private fun initializeRecyclerView() {
        binding.rvChats.apply {
            adapter = chatsAdapter
            addItemDecoration(CustomDivider(10))
            layoutManager = LinearLayoutManager(context)
        }
    }

}