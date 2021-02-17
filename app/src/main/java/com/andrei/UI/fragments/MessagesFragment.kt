package com.andrei.UI.fragments

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.andrei.carrental.R
import com.andrei.carrental.entities.Message
import com.andrei.carrental.viewmodels.ViewModelChat
import com.andrei.utils.observeOnce

class MessagesFragment :BaseFragment(R.layout.fragment_messages) {

    private val viewModelChat:ViewModelChat by activityViewModels()

    private val observerLastMessage = Observer<Message>{

    }

    private val observerChatMessages = Observer<List<Message>>{
        startObservingLastMessages()
    }


    override fun initializeUI() {
       // viewModelChat.setCurrentOpenedChatID()
        //observe the messages once and then for performance reasons only observer once
    }

    private fun startObservingLastMessages(){

    }

}