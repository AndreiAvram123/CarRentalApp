package com.andrei.carrental.entities

import androidx.lifecycle.LiveData
import com.andrei.engine.DTOEntities.MessageDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow


data class ObservableChat(
    val id:Long,
    val friend:User,
    val isUserOnline:StateFlow<Boolean>,
    val lastMessage: Flow<Message>
)
