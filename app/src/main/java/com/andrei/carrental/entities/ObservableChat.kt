package com.andrei.carrental.entities

import androidx.lifecycle.LiveData
import com.andrei.engine.DTOEntities.Chat
import com.andrei.engine.DTOEntities.MessageDTO
import com.stfalcon.chatkit.commons.models.IDialog
import com.stfalcon.chatkit.commons.models.IUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow


data class ObservableChat(
    val chat:Chat,
    val isUserOnline:StateFlow<Boolean>,
    val lastMessage: StateFlow<Message>
)
