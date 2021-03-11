package com.andrei.engine.repository.interfaces

import androidx.lifecycle.LiveData
import com.andrei.carrental.entities.Message
import com.andrei.carrental.helpers.ConsumeLiveData
import com.andrei.engine.DTOEntities.ChatDTO
import com.andrei.engine.State
import com.andreia.carrental.requestModels.CreateMessageRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

interface ChatRepository {
    val messageToUnsendState: MutableSharedFlow<State<Message>>
    suspend fun getInitialChatMessages(chatID: Long): List<Message>
    suspend fun sendMessage(createMessageRequest: CreateMessageRequest)
    suspend fun unsendMessage(message:Message)
    suspend fun fetchUserChats(): Flow<State<List<ChatDTO>>>
    val messageToSendState: MutableSharedFlow<State<Message>>
}