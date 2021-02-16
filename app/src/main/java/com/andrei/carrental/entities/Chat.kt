package com.andrei.carrental.entities

import androidx.lifecycle.LiveData
import com.andrei.services.ChannelService

data class Chat(
    val friend:User,
    val isUserOnline:LiveData<Boolean>,
    val lastMessage:LiveData<String>
)
