package com.andrei.engine.repository.interfaces

import androidx.lifecycle.LiveData
import com.andrei.carrental.entities.Message
import com.andrei.engine.DTOEntities.ChatDTO
import com.andrei.engine.DTOEntities.MessageDTO
import com.andrei.engine.State
import com.andreia.carrental.requestModels.CreateMessageRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

interface ChatRepository {
    val messageToUnsendState: MutableSharedFlow<State<Message>>
    val messageToSendState: MutableSharedFlow<State<Message>>


    suspend fun getInitialChatMessages(chatID: Long): List<Message>
    suspend fun sendMessage(createMessageRequest: CreateMessageRequest)
    suspend fun unsendMessage(message:Message)
    suspend fun fetchUserChats(userID:Long): Flow<State<List<ChatDTO>>>
    fun loadMoreMessages(chatID: Long, offset: Int): Flow<State<List<Message>>>
}