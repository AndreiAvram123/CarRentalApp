package com.andrei.carrental.entities

import androidx.lifecycle.LiveData
import com.andrei.services.ChannelService
import com.andreia.carrental.entities.Message

data class Chat(
    val friend:User,
    val isUserOnline:LiveData<Boolean>,
    val lastMessage:LiveData<Message>
)
