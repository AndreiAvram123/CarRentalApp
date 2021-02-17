package com.andrei.carrental.entities

import androidx.lifecycle.LiveData
import com.andrei.engine.DTOEntities.MessageDTO

data class Chat(
    val friend:User,
    val isUserOnline:LiveData<Boolean>,
    val lastMessageDTO:LiveData<Message>
)
