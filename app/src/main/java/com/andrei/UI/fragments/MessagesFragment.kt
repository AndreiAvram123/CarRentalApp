package com.andrei.UI.fragments

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.andrei.UI.adapters.CustomDivider
import com.andrei.UI.adapters.messages.MessagesRVAdapter
import com.andrei.carrental.R
import com.andrei.carrental.databinding.FragmentMessagesBinding
import com.andrei.carrental.entities.Image
import com.andrei.carrental.viewmodels.ViewModelChat
import com.andrei.utils.reObserve
import kotlinx.coroutines.launch

class MessagesFragment :BaseFragment(R.layout.fragment_messages) {

    private val binding:FragmentMessagesBinding by viewBinding ()
    private val viewModelChat:ViewModelChat by activityViewModels()
    private val navArgs:MessagesFragmentArgs by navArgs()
    private val messagesAdapter:MessagesRVAdapter by lazy{
        MessagesRVAdapter(this::expandImage)
    }


    override fun initializeUI() {
        viewModelChat.setCurrentOpenedChatID(navArgs.chatID)
        initializeRecyclerView()
    }
    private fun populateRVWithData(){
        lifecycleScope.launch {
            val data = viewModelChat.getInitialChatMessages()
            messagesAdapter.setData(data)
            startObservingLastMessage()
        }
    }

    private fun initializeRecyclerView() {
        binding.rvMessages.apply {
            adapter = messagesAdapter
            addItemDecoration(CustomDivider(10))
            layoutManager = LinearLayoutManager(context)
        }
        populateRVWithData()
    }

    private fun startObservingLastMessage(){
          viewModelChat.lastChatMessage.reObserve(viewLifecycleOwner){
             messagesAdapter.addMessage(it)
          }
    }

    private fun expandImage(image:Image){

    }

}