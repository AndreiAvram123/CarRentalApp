package com.andrei.engine.repository.interfaces

import androidx.lifecycle.LiveData
import com.andrei.carrental.entities.Message
import com.andrei.engine.DTOEntities.ChatDTO
import com.andrei.engine.State

interface ChatRepository {
    val userChats: LiveData<State<List<ChatDTO>>>
    suspend fun getInitialChatMessages(chatID: Long): List<Message>
    suspend fun sendMessage(text: String, currentChatID: Long)
    fun unsendMessage(messages:Message) :LiveData<State<Message>>
}