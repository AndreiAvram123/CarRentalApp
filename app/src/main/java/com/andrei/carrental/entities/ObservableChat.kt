package com.andrei.carrental.entities

import androidx.lifecycle.LiveData
import com.andrei.engine.DTOEntities.MessageDTO

data class ObservableChat(
    val id:Long,
    val friend:User,
    val isUserOnline:LiveData<Boolean>,
    val lastMessageDTO:LiveData<Message>
)
