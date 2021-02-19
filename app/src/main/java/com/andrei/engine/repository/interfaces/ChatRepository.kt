package com.andrei.engine.repository.interfaces

import androidx.lifecycle.LiveData
import com.andrei.carrental.entities.Message
import com.andrei.carrental.helpers.ConsumeLiveData
import com.andrei.engine.DTOEntities.ChatDTO
import com.andrei.engine.State

interface ChatRepository {
    val userChats: LiveData<State<List<ChatDTO>>>
    val messageToUnsendState: ConsumeLiveData<State<Message>>
    suspend fun getInitialChatMessages(chatID: Long): List<Message>
    suspend fun sendMessage(text: String, currentChatID: Long)
    suspend fun unsendMessage(message:Message)
}