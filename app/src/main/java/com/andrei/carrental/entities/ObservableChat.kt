package com.andrei.carrental.entities

import androidx.lifecycle.LiveData
import com.andrei.engine.DTOEntities.MessageDTO
import kotlinx.coroutines.flow.Flow


data class ObservableChat(
    val id:Long,
    val friend:User,
    val isUserOnline:LiveData<Boolean>,
    val lastMessage: Flow<Message>
)
